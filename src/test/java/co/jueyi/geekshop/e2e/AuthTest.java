/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.e2e;

import co.jueyi.geekshop.*;
import co.jueyi.geekshop.common.Constant;
import co.jueyi.geekshop.config.TestConfig;
import co.jueyi.geekshop.types.administrator.CreateAdministratorInput;
import co.jueyi.geekshop.types.auth.CurrentUser;
import co.jueyi.geekshop.types.common.CreateCustomerInput;
import co.jueyi.geekshop.types.common.Permission;
import co.jueyi.geekshop.types.customer.CustomerList;
import co.jueyi.geekshop.types.role.CreateRoleInput;
import co.jueyi.geekshop.types.role.Role;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.GraphQLResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * Created on Nov, 2020 by @author bobo
 */
@GeekShopGraphQLTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
public class AuthTest {

    public static final String SHARED_GRAPHQL_RESOURCE_TEMPLATE = "graphql/shared/%s.graphqls";
    public static final String ADMIN_ME =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "admin_me");
    public static final String CURRENT_USER_FRAGMENT =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "current_user_fragment");
    public static final String ATTEMPT_LOGIN =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "admin_attempt_login");
    public static final String GET_CUSTOMER_LIST =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "get_customer_list");
    public static final String CREATE_ROLE =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "create_role");
    public static final String ROLE_FRAGMENT =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "role_fragment");
    public static final String CREATE_ADMINISTRATOR =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "create_administrator");
    public static final String ADMINISTRATOR_FRAGMENT =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "administrator_fragment");

    public static final String ADMIN_AUTH_GRAPHQL_RESOURCE_TEMPLATE = "graphql/admin/auth/%s.graphqls";
    public static final String CREATE_CUSTOMER =
            String.format(ADMIN_AUTH_GRAPHQL_RESOURCE_TEMPLATE, "create_customer");
    public static final String GET_CUSTOMER_COUNT =
            String.format(ADMIN_AUTH_GRAPHQL_RESOURCE_TEMPLATE, "get_customer_count");

    @Autowired
    @Qualifier(TestConfig.ADMIN_CLIENT_BEAN)
    ApiClient adminClient;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockDataService mockDataService;

    @BeforeAll
    void beforeAll() throws IOException {
        PopulateOptions populateOptions = PopulateOptions.builder().customerCount(1).build();
        populateOptions.setInitialData(TestHelper.getInitialData());
        populateOptions.setProductCsvPath(TestHelper.getTestFixture("e2e-products-minimal.csv"));

        mockDataService.populate(populateOptions);
    }

    /**
     * admin permissions test suite
     */
    @Test
    @Order(1)
    public void me_is_not_permitted_for_anonymous_user() throws IOException {
        // Anonymous user
        this.adminClient.asAnonymousUser();

        try {
            this.adminClient.perform(ADMIN_ME, null, Arrays.asList(CURRENT_USER_FRAGMENT));
            fail("should have thrown");
        } catch (ApiException apiEx) {
            assertThat(apiEx.getMessage()).isEqualTo("You are not currently authorized to perform this action");
        }
    }

    @Test
    @Order(2)
    public void anonymous_user_can_attempt_login() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("username", Constant.SUPER_ADMIN_USER_IDENTIFIER);
        variables.put("password", Constant.SUPER_ADMIN_USER_PASSWORD);

        GraphQLResponse graphQLResponse =
                this.adminClient.perform(ATTEMPT_LOGIN, variables, Arrays.asList(CURRENT_USER_FRAGMENT));
        assertThat(graphQLResponse.isOk());
    }

    /**
     * Customer user test
     */
    @Test
    @Order(3)
    public void customer_user_cannot_login() throws IOException {
        this.adminClient.asSuperAdmin();
        GraphQLResponse graphQLResponse = this.adminClient.perform(GET_CUSTOMER_LIST, null);
        assertThat(graphQLResponse.isOk());
        CustomerList customerList = graphQLResponse.get("$.data.customers", CustomerList.class);
        assertThat(customerList.getItems()).hasSize(1);
        String customerEmailAddress = customerList.getItems().get(0).getEmailAddress();

        try {
            this.adminClient.asUserWithCredentials(customerEmailAddress, MockDataService.TEST_PASSWORD);
            fail("should have thrown");
        } catch (ApiException apiEx) {
            assertThat(apiEx.getMessage()).isEqualTo("The credentials did not match. Please check and try again");
        }
    }

    /**
     * ReadCatalog permission test suite
     */

    @Test
    @Order(4)
    public void me_returns_correct_permissions_after_adding_read_catalog_permission() throws IOException {
        this.adminClient.asSuperAdmin();
        Pair<String, String> pair =
                this.createAdministratorWithPermissions("ReadCatalog", Arrays.asList(Permission.ReadCatalog));
        this.adminClient.asUserWithCredentials(pair.getLeft(), pair.getRight());

        GraphQLResponse graphQLResponse =
                this.adminClient.perform(ADMIN_ME, null, Arrays.asList(CURRENT_USER_FRAGMENT));
        assertThat(graphQLResponse.isOk());

        CurrentUser currentUser = graphQLResponse.get("$.data.adminMe", CurrentUser.class);
        assertThat(currentUser.getPermissions()).containsExactlyInAnyOrder(
                Permission.Authenticated, Permission.ReadCatalog
        );
    }

    // TODO add read/update/create product test

    /**
     * CRUD on Customers permissions test suite
     */
    @Test
    @Order(5)
    public void me_returns_correct_permissions_after_adding_crud_on_customers_permission() throws IOException {
        this.adminClient.asSuperAdmin();
        Pair<String, String> pair = this.createAdministratorWithPermissions("CRUDCustomer",
                        Arrays.asList(
                                Permission.ReadCustomer,
                                Permission.CreateCustomer,
                                Permission.UpdateCustomer,
                                Permission.DeleteCustomer));
        this.adminClient.asUserWithCredentials(pair.getLeft(), pair.getRight());

        GraphQLResponse graphQLResponse =
                this.adminClient.perform(ADMIN_ME, null, Arrays.asList(CURRENT_USER_FRAGMENT));
        assertThat(graphQLResponse.isOk());

        CurrentUser currentUser = graphQLResponse.get("$.data.adminMe", CurrentUser.class);
        assertThat(currentUser.getPermissions()).containsExactlyInAnyOrder(
                Permission.ReadCustomer,
                Permission.CreateCustomer,
                Permission.UpdateCustomer,
                Permission.DeleteCustomer,
                Permission.Authenticated
        );
    }

    @Test
    @Order(6)
    public void can_create_customer() throws IOException {
        CreateCustomerInput createCustomerInput = new CreateCustomerInput();
        createCustomerInput.setEmailAddress("");
        createCustomerInput.setFirstName("");
        createCustomerInput.setLastName("");

        JsonNode inputNode = objectMapper.valueToTree(createCustomerInput);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        GraphQLResponse graphQLResponse = this.adminClient.perform(CREATE_CUSTOMER, variables);
        assertThat(graphQLResponse.isOk());
    }

    @Test
    @Order(7)
    public void can_read_customer() throws IOException {
        GraphQLResponse graphQLResponse = this.adminClient.perform(GET_CUSTOMER_COUNT, null);
        assertThat(graphQLResponse.isOk());
        CustomerList customerList = graphQLResponse.get("$.data.customers", CustomerList.class);
        assertThat(customerList.getTotalItems()).isEqualTo(2);
    }

    private Pair<String, String> createAdministratorWithPermissions(String code, List<Permission> permissions)
            throws IOException {
        CreateRoleInput input = new CreateRoleInput();
        input.setCode(code);
        input.setDescription("");
        input.setPermissions(permissions);

        JsonNode inputNode = objectMapper.valueToTree(input);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        GraphQLResponse graphQLResponse = this.adminClient.perform(CREATE_ROLE, variables, Arrays.asList(ROLE_FRAGMENT));
        assertThat(graphQLResponse.isOk());
        Role role = graphQLResponse.get("$.data.createRole", Role.class);

        String identifier = code + "@" + RandomStringUtils.randomAlphabetic(7);
        String password = MockDataService.TEST_PASSWORD;

        CreateAdministratorInput createAdministratorInput = new CreateAdministratorInput();
        createAdministratorInput.setEmailAddress(identifier);
        createAdministratorInput.setFirstName(code);
        createAdministratorInput.setLastName("Admin");
        createAdministratorInput.setPassword(password);
        createAdministratorInput.getRoleIds().add(role.getId());

        inputNode = objectMapper.valueToTree(createAdministratorInput);
        variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);
        graphQLResponse = this.adminClient.perform(CREATE_ADMINISTRATOR, variables, Arrays.asList(ADMINISTRATOR_FRAGMENT));
        assertThat(graphQLResponse.isOk());

        return Pair.of(identifier, password);
    }
}
