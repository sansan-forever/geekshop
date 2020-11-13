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
import co.jueyi.geekshop.types.address.Address;
import co.jueyi.geekshop.types.common.CreateAddressInput;
import co.jueyi.geekshop.types.common.UpdateAddressInput;
import co.jueyi.geekshop.types.customer.Customer;
import co.jueyi.geekshop.types.customer.CustomerList;
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

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;

/**
 * Created on Nov, 2020 by @author bobo
 */
@GeekShopGraphQLTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
public class CustomerTest {

    static final String SHARED_GRAPHQL_RESOURCE_TEMPLATE = "graphql/shared/%s.graphqls";
    static final String GET_CUSTOMER_LIST =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "get_customer_list");
    static final String GET_CUSTOMER =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "get_customer");
    static final String CUSTOMER_FRAGMENT =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "customer_fragment");
    static final String ADDRESS_FRAGMENT =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "address_fragment");

    static final String ADMIN_CUSTOMER_GRAPHQL_RESOURCE_TEMPLATE = "graphql/admin/customer/%s.graphqls";
    static final String GET_CUSTOMER_WITH_USER =
            String.format(ADMIN_CUSTOMER_GRAPHQL_RESOURCE_TEMPLATE, "get_customer_with_user");
    static final String CREATE_ADDRESS =
            String.format(ADMIN_CUSTOMER_GRAPHQL_RESOURCE_TEMPLATE, "create_address");
    static final String UPDATE_ADDRESS =
            String.format(ADMIN_CUSTOMER_GRAPHQL_RESOURCE_TEMPLATE, "update_address");
    static final String DELETE_CUSTOMER_ADDRESS =
            String.format(ADMIN_CUSTOMER_GRAPHQL_RESOURCE_TEMPLATE, "delete_customer_address");

    @Autowired
    @Qualifier(TestConfig.ADMIN_CLIENT_BEAN)
    ApiClient adminClient;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockDataService mockDataService;

    @BeforeAll
    void beforeAll() throws IOException {
        mockDataService.populate(PopulateOptions.builder().customerCount(5).build());
        adminClient.asSuperAdmin();
    }

    Customer firstCustomer;
    Customer secondCustomer;
    Customer thirdCustomer;

    /**
     * Customer resolver
     */

    @Test
    @Order(1)
    public void customer_list() throws IOException {
        GraphQLResponse graphQLResponse = this.adminClient.perform(GET_CUSTOMER_LIST, null);
        assertThat(graphQLResponse.isOk());
        CustomerList customerList = graphQLResponse.get("$.data.customers", CustomerList.class);
        assertThat(customerList.getTotalItems()).isEqualTo(5);
        assertThat(customerList.getItems()).hasSize(5);
        firstCustomer = customerList.getItems().get(0);
        secondCustomer = customerList.getItems().get(1);
        thirdCustomer = customerList.getItems().get(2);
    }

    @Test
    @Order(2)
    public void customer_resolver_resolves_user() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", firstCustomer.getId());

        GraphQLResponse graphQLResponse =
                this.adminClient.perform(GET_CUSTOMER_WITH_USER, variables);
        assertThat(graphQLResponse.isOk());
        Customer customer = graphQLResponse.get("$.data.customer", Customer.class);
        assertThat(customer.getUser()).isNotNull();
        assertThat(customer.getUser().getId()).isEqualTo(2);
        assertThat(customer.getUser().getIdentifier()).isEqualTo(firstCustomer.getEmailAddress());
        assertThat(customer.getUser().getVerified()).isTrue();
    }

    /**
     * address
     */

    List<Long> firstCustomerAddressIds;
    Long firstCustomerThirdAddressId;

    @Test
    @Order(3)
    public void createCustomerAddress_creates_a_new_address() throws IOException {
        CreateAddressInput createAddressInput = new CreateAddressInput();
        createAddressInput.setFullName("fullName");
        createAddressInput.setCompany("company");
        createAddressInput.setStreetLine1("streetLine1");
        createAddressInput.setStreetLine2("streetLine2");
        createAddressInput.setCity("city");
        createAddressInput.setProvince("province");
        createAddressInput.setPostalCode("postalCode");
        createAddressInput.setPhoneNumber("18008887788");
        createAddressInput.setDefaultShippingAddress(false);
        createAddressInput.setDefaultBillingAddress(false);

        JsonNode inputNode = objectMapper.valueToTree(createAddressInput);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", firstCustomer.getId());
        variables.set("input", inputNode);

        GraphQLResponse graphQLResponse = this.adminClient.perform(CREATE_ADDRESS, variables);
        assertThat(graphQLResponse.isOk());

        Address address = graphQLResponse.get("$.data.adminCreateCustomerAddress", Address.class);
        assertThat(address.getFullName()).isEqualTo(createAddressInput.getFullName());
        assertThat(address.getCompany()).isEqualTo(createAddressInput.getCompany());
        assertThat(address.getStreetLine1()).isEqualTo(createAddressInput.getStreetLine1());
        assertThat(address.getStreetLine2()).isEqualTo(createAddressInput.getStreetLine2());
        assertThat(address.getCity()).isEqualTo(createAddressInput.getCity());
        assertThat(address.getProvince()).isEqualTo(createAddressInput.getProvince());
        assertThat(address.getPostalCode()).isEqualTo(createAddressInput.getPostalCode());
        assertThat(address.getPhoneNumber()).isEqualTo(createAddressInput.getPhoneNumber());
        assertThat(address.getDefaultBillingAddress()).isEqualTo(createAddressInput.getDefaultBillingAddress());
        assertThat(address.getDefaultShippingAddress()).isEqualTo(createAddressInput.getDefaultShippingAddress());
    }

    @Test
    @Order(4)
    public void customer_query_returns_addresses() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", firstCustomer.getId());

        GraphQLResponse graphQLResponse =
                this.adminClient.perform(GET_CUSTOMER, variables, Arrays.asList(CUSTOMER_FRAGMENT, ADDRESS_FRAGMENT));
        assertThat(graphQLResponse.isOk());

        Customer customer = graphQLResponse.get("$.data.customer", Customer.class);
        assertThat(customer.getAddresses()).hasSize(2);
        firstCustomerAddressIds =
                customer.getAddresses().stream().map(address -> address.getId()).collect(toList());
    }

    @Test
    @Order(5)
    public void updateCustomerAddress_updates_the_city() throws IOException {
        UpdateAddressInput input = new UpdateAddressInput();
        input.setId(firstCustomerAddressIds.get(0));
        input.setCity("updated_city");

        JsonNode inputNode = objectMapper.valueToTree(input);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        GraphQLResponse graphQLResponse = this.adminClient.perform(UPDATE_ADDRESS, variables);
        Address address = graphQLResponse.get("$.data.adminUpdateCustomerAddress", Address.class);
        assertThat(address.getCity()).isEqualTo(input.getCity());
    }

    @Test
    @Order(6)
    public void updateCustomerAddress_allows_only_a_single_default_address() throws IOException {
        // set the first customer's second address to be default
        UpdateAddressInput input = new UpdateAddressInput();
        input.setId(firstCustomerAddressIds.get(1));
        input.setDefaultShippingAddress(true);
        input.setDefaultBillingAddress(true);

        JsonNode inputNode = objectMapper.valueToTree(input);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        GraphQLResponse graphQLResponse = this.adminClient.perform(UPDATE_ADDRESS, variables);
        Address address = graphQLResponse.get("$.data.adminUpdateCustomerAddress", Address.class);
        assertThat(address.getDefaultShippingAddress()).isTrue();
        assertThat(address.getDefaultBillingAddress()).isTrue();

        // asset the first customer's other address is not default
        variables = objectMapper.createObjectNode();
        variables.put("id", firstCustomer.getId());

        graphQLResponse =
                this.adminClient.perform(GET_CUSTOMER, variables, Arrays.asList(CUSTOMER_FRAGMENT, ADDRESS_FRAGMENT));
        assertThat(graphQLResponse.isOk());

        Customer customer = graphQLResponse.get("$.data.customer", Customer.class);
        Address otherAddress =
                customer.getAddresses().stream().filter(a -> !a.getId().equals(firstCustomerAddressIds.get(1)))
                .collect(toList()).get(0);
        assertThat(otherAddress.getDefaultBillingAddress()).isFalse();
        assertThat(otherAddress.getDefaultShippingAddress()).isFalse();

        // set the first customer's first address to be default
        input = new UpdateAddressInput();
        input.setId(firstCustomerAddressIds.get(0));
        input.setDefaultShippingAddress(true);
        input.setDefaultBillingAddress(true);

        inputNode = objectMapper.valueToTree(input);
        variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        graphQLResponse = this.adminClient.perform(UPDATE_ADDRESS, variables);
        address = graphQLResponse.get("$.data.adminUpdateCustomerAddress", Address.class);
        assertThat(address.getDefaultShippingAddress()).isTrue();
        assertThat(address.getDefaultBillingAddress()).isTrue();

        // assert the first customer's second address is not default
        variables = objectMapper.createObjectNode();
        variables.put("id", firstCustomer.getId());

        graphQLResponse =
                this.adminClient.perform(GET_CUSTOMER, variables, Arrays.asList(CUSTOMER_FRAGMENT, ADDRESS_FRAGMENT));
        assertThat(graphQLResponse.isOk());

        customer = graphQLResponse.get("$.data.customer", Customer.class);
        otherAddress =
                customer.getAddresses().stream().filter(a -> !a.getId().equals(firstCustomerAddressIds.get(0)))
                        .collect(toList()).get(0);
        assertThat(otherAddress.getDefaultBillingAddress()).isFalse();
        assertThat(otherAddress.getDefaultShippingAddress()).isFalse();

        // get the second customer's address id
        variables = objectMapper.createObjectNode();
        variables.put("id", secondCustomer.getId());

        graphQLResponse =
                this.adminClient.perform(GET_CUSTOMER, variables, Arrays.asList(CUSTOMER_FRAGMENT, ADDRESS_FRAGMENT));
        assertThat(graphQLResponse.isOk());

        customer = graphQLResponse.get("$.data.customer", Customer.class);
        Long secondCustomerAddressId = customer.getAddresses().get(0).getId();

        // set the second customer's address to be default
        input = new UpdateAddressInput();
        input.setId(secondCustomerAddressId);
        input.setDefaultShippingAddress(true);
        input.setDefaultBillingAddress(true);

        inputNode = objectMapper.valueToTree(input);
        variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        graphQLResponse = this.adminClient.perform(UPDATE_ADDRESS, variables);
        address = graphQLResponse.get("$.data.adminUpdateCustomerAddress", Address.class);
        assertThat(address.getDefaultShippingAddress()).isTrue();
        assertThat(address.getDefaultBillingAddress()).isTrue();

        // assets the first customer's address defaults are unchanged
        variables = objectMapper.createObjectNode();
        variables.put("id", firstCustomer.getId());

        graphQLResponse =
                this.adminClient.perform(GET_CUSTOMER, variables, Arrays.asList(CUSTOMER_FRAGMENT, ADDRESS_FRAGMENT));
        assertThat(graphQLResponse.isOk());
        customer = graphQLResponse.get("$.data.customer", Customer.class);

        assertThat(customer.getAddresses().get(0).getDefaultShippingAddress()).isTrue();
        assertThat(customer.getAddresses().get(0).getDefaultBillingAddress()).isTrue();
        assertThat(customer.getAddresses().get(1).getDefaultShippingAddress()).isFalse();
        assertThat(customer.getAddresses().get(1).getDefaultBillingAddress()).isFalse();
    }


    @Test
    @Order(6)
    public void createCustomerAddress_with_true_defaults_unsets_existing_defaults() throws IOException {
        CreateAddressInput input = new CreateAddressInput();
        input.setStreetLine1("new default streetline");
        input.setCity("new_city");
        input.setDefaultShippingAddress(true);
        input.setDefaultBillingAddress(true);

        JsonNode inputNode = objectMapper.valueToTree(input);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", firstCustomer.getId());
        variables.set("input", inputNode);

        GraphQLResponse graphQLResponse = this.adminClient.perform(CREATE_ADDRESS, variables);
        assertThat(graphQLResponse.isOk());
        Address createCustomerAddress = graphQLResponse.get("$.data.adminCreateCustomerAddress", Address.class);
        assertThat(createCustomerAddress.getFullName()).isEmpty();
        assertThat(createCustomerAddress.getCompany()).isEmpty();
        assertThat(createCustomerAddress.getStreetLine1()).isEqualTo(input.getStreetLine1());
        assertThat(createCustomerAddress.getStreetLine2()).isEmpty();
        assertThat(createCustomerAddress.getCity()).isEqualTo(input.getCity());
        assertThat(createCustomerAddress.getProvince()).isEmpty();
        assertThat(createCustomerAddress.getPostalCode()).isEmpty();
        assertThat(createCustomerAddress.getPhoneNumber()).isEmpty();
        assertThat(createCustomerAddress.getDefaultShippingAddress()).isTrue();
        assertThat(createCustomerAddress.getDefaultBillingAddress()).isTrue();

        variables = objectMapper.createObjectNode();
        variables.put("id", firstCustomer.getId());
        graphQLResponse =
                this.adminClient.perform(GET_CUSTOMER, variables, Arrays.asList(CUSTOMER_FRAGMENT, ADDRESS_FRAGMENT));
        assertThat(graphQLResponse.isOk());
        Customer customer = graphQLResponse.get("$.data.customer", Customer.class);
        for(Address address : customer.getAddresses()) {
            boolean shouldBeDefault = createCustomerAddress.getId().equals(address.getId());
            assertThat(address.getDefaultShippingAddress()).isEqualTo(shouldBeDefault);
            assertThat(address.getDefaultBillingAddress()).isEqualTo(shouldBeDefault);
        }

        firstCustomerThirdAddressId = createCustomerAddress.getId();
    }

    @Test
    @Order(7)
    public void deleteCustomerAddress_on_default_address_resets_defaults() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", firstCustomerThirdAddressId);

        GraphQLResponse graphQLResponse = this.adminClient.perform(DELETE_CUSTOMER_ADDRESS, variables);
        assertThat(graphQLResponse.isOk());
        Boolean deleteCustomerAddress = graphQLResponse.get("$.data.adminDeleteCustomerAddress", Boolean.class);
        assertThat(deleteCustomerAddress).isTrue();

        variables = objectMapper.createObjectNode();
        variables.put("id", firstCustomer.getId());
        graphQLResponse =
                this.adminClient.perform(GET_CUSTOMER, variables, Arrays.asList(CUSTOMER_FRAGMENT, ADDRESS_FRAGMENT));
        assertThat(graphQLResponse.isOk());
        Customer customer = graphQLResponse.get("$.data.customer", Customer.class);
        assertThat(customer.getAddresses()).hasSize(2);

        List<Address> defaultAddresses = customer.getAddresses()
                .stream().filter(a -> a.getDefaultBillingAddress() && a.getDefaultShippingAddress())
                .collect(toList());
        List<Address> otherAddresses = customer.getAddresses()
                .stream().filter(a -> !a.getDefaultBillingAddress() && !a.getDefaultShippingAddress())
                .collect(toList());
        assertThat(defaultAddresses).hasSize(1);
        assertThat(otherAddresses).hasSize(1);
    }

    // TODO orders


}
