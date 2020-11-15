/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.e2e;

import co.jueyi.geekshop.ApiClient;
import co.jueyi.geekshop.GeekShopGraphQLTest;
import co.jueyi.geekshop.MockDataService;
import co.jueyi.geekshop.PopulateOptions;
import co.jueyi.geekshop.config.TestConfig;
import co.jueyi.geekshop.types.customer.*;
import co.jueyi.geekshop.types.history.HistoryEntry;
import co.jueyi.geekshop.types.history.HistoryEntryListOptions;
import co.jueyi.geekshop.types.history.HistoryEntryType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.GraphQLResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

/**
 * Created on Nov, 2020 by @author bobo
 */
@GeekShopGraphQLTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
public class CustomerGroupTest {

    static final String SHARED_GRAPHQL_RESOURCE_TEMPLATE = "graphql/shared/%s.graphqls";
    static final String GET_CUSTOMER_LIST =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "get_customer_list");
    static final String GET_CUSTOMER_HISTORY =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "get_customer_history");

    static final String CREATE_CUSTOMER_GROUP =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "create_customer_group");
    static final String CUSTOMER_GROUP_FRAGMENT =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "customer_group_fragment");

    static final String CUSTOMER_GROUP_GRAPHQL_RESOURCE_TEMPLATE = "graphql/admin/customer_group/%s.graphqls";
    static final String GET_CUSTOMER_GROUPS =
            String.format(CUSTOMER_GROUP_GRAPHQL_RESOURCE_TEMPLATE, "get_customer_groups");
    static final String GET_CUSTOMER_GROUP =
            String.format(CUSTOMER_GROUP_GRAPHQL_RESOURCE_TEMPLATE, "get_customer_group");
    static final String UPDATE_CUSTOMER_GROUP =
            String.format(CUSTOMER_GROUP_GRAPHQL_RESOURCE_TEMPLATE, "update_customer_group");

    @Autowired
    @Qualifier(TestConfig.ADMIN_CLIENT_BEAN)
    ApiClient adminClient;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockDataService mockDataService;

    List<Customer> customers;

    @BeforeAll
    void beforeAll() throws IOException {
        mockDataService.populate(PopulateOptions.builder().customerCount(5).build());
        adminClient.asSuperAdmin();

        customers = getCustomerList().getItems();
    }

    @Test
    @Order(1)
    public void create() throws IOException {
        CreateCustomerGroupInput input = new CreateCustomerGroupInput();
        input.setName("group 1");
        input.getCustomerIds().add(customers.get(0).getId());
        input.getCustomerIds().add(customers.get(1).getId());

        JsonNode inputNode = objectMapper.valueToTree(input);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        GraphQLResponse graphQLResponse =
                this.adminClient.perform(CREATE_CUSTOMER_GROUP, variables, Arrays.asList(CUSTOMER_GROUP_FRAGMENT));
        assertThat(graphQLResponse.isOk());

        CustomerGroup customerGroup = graphQLResponse.get("$.data.createCustomerGroup", CustomerGroup.class);
        assertThat(customerGroup.getName()).isEqualTo(input.getName());
        assertThat(customerGroup.getCustomers().getItems().stream().map(Customer::getId).collect(Collectors.toSet()))
                .containsExactlyInAnyOrder(customers.get(0).getId(), customers.get(1).getId());
    }

    @Test
    @Order(2)
    public void history_entry_for_CUSTOMER_ADDED_TO_GROUP_after_group_created() throws IOException {
        HistoryEntryListOptions options = new HistoryEntryListOptions();
        options.setPageSize(3);
        options.setCurrentPage(2);
        JsonNode optionsNote = objectMapper.valueToTree(options);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", customers.get(0).getId());
        variables.set("options", optionsNote);

        GraphQLResponse graphQLResponse = adminClient.perform(GET_CUSTOMER_HISTORY, variables);
        assertThat(graphQLResponse.isOk());

        Customer customer = graphQLResponse.get("$.data.customer", Customer.class);
        assertThat(customer.getHistory().getItems()).hasSize(1);
        HistoryEntry historyEntry = customer.getHistory().getItems().get(0);
        assertThat(historyEntry.getType()).isEqualTo(HistoryEntryType.CUSTOMER_ADDED_TO_GROUP);
        assertThat(historyEntry.getData()).containsEntry("groupName", "group 1");
    }

    @Test
    @Order(3)
    public void customerGroups() throws IOException {
        GraphQLResponse graphQLResponse = this.adminClient.perform(GET_CUSTOMER_GROUPS, null);
        CustomerGroupList customerGroupList =
                graphQLResponse.get("$.data.customerGroups", CustomerGroupList.class);

        assertThat(customerGroupList.getTotalItems()).isEqualTo(1);
        assertThat(customerGroupList.getItems().get(0).getName()).isEqualTo("group 1");
    }

    private CustomerList getCustomerList() throws IOException {
        GraphQLResponse graphQLResponse = this.adminClient.perform(GET_CUSTOMER_LIST, null);
        assertThat(graphQLResponse.isOk());
        CustomerList customerList = graphQLResponse.get("$.data.customers", CustomerList.class);
        return customerList;
    }

    @Test
    @Order(4)
    public void customerGroup_with_customer_list_options() throws IOException {
        CustomerGroupListOptions options = new CustomerGroupListOptions();
        options.setPageSize(1);
        options.setCurrentPage(1);

        JsonNode optionsNode = objectMapper.valueToTree(options);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", 1L);
        variables.set("options", optionsNode);

        GraphQLResponse graphQLResponse = this.adminClient.perform(GET_CUSTOMER_GROUP, variables);
        CustomerGroup customerGroup = graphQLResponse.get("$.data.customerGroup", CustomerGroup.class);
        assertThat(customerGroup.getId()).isEqualTo(1L);
        assertThat(customerGroup.getName()).isEqualTo("group 1");
        assertThat(customerGroup.getCustomers().getItems()).hasSize(1);
        assertThat(customerGroup.getCustomers().getTotalItems()).isEqualTo(2);
    }

    @Test
    @Order(5)
    public void update() throws IOException {
        UpdateCustomerGroupInput input = new UpdateCustomerGroupInput();
        input.setId(1L);
        input.setName("group 1 updated");

        JsonNode inputNode = objectMapper.valueToTree(input);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        GraphQLResponse graphQLResponse =
                this.adminClient.perform(UPDATE_CUSTOMER_GROUP, variables, Arrays.asList(CUSTOMER_GROUP_FRAGMENT));
        CustomerGroup customerGroup = graphQLResponse.get("$.data.updateCustomerGroup", CustomerGroup.class);
        assertThat(customerGroup.getName()).isEqualTo(input.getName());
    }
}
