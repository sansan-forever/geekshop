/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.e2e;

import co.jueyi.geekshop.*;
import co.jueyi.geekshop.common.Constant;
import co.jueyi.geekshop.config.TestConfig;
import co.jueyi.geekshop.config.auth.TestAuthenticationStrategy;
import co.jueyi.geekshop.config.auth.UserData;
import co.jueyi.geekshop.service.HistoryService;
import co.jueyi.geekshop.types.auth.AuthenticationInput;
import co.jueyi.geekshop.types.auth.CurrentUser;
import co.jueyi.geekshop.types.auth.LoginResult;
import co.jueyi.geekshop.types.customer.Customer;
import co.jueyi.geekshop.types.customer.CustomerList;
import co.jueyi.geekshop.types.customer.RegisterCustomerInput;
import co.jueyi.geekshop.types.history.HistoryEntry;
import co.jueyi.geekshop.types.history.HistoryEntryListOptions;
import co.jueyi.geekshop.types.history.HistoryEntryType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;
import com.graphql.spring.boot.test.GraphQLResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

/**
 * Created on Nov, 2020 by @author bobo
 */
@GeekShopGraphQLTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
public class AuthenticationStrategyTest {

    public static final String AUTHENTICATION_STRATEGY_GRAPHQL_RESOURCE_TEMPLATE =
            "graphql/authentication_strategy/%s.graphqls";
    public static final String AUTHENTICATE =
            String.format(AUTHENTICATION_STRATEGY_GRAPHQL_RESOURCE_TEMPLATE, "authenticate");
    public static final String GET_CUSTOMER_USER_AUTH =
            String.format(AUTHENTICATION_STRATEGY_GRAPHQL_RESOURCE_TEMPLATE, "get_customer_user_auth");

    public static final String SHARED_GRAPHQL_RESOURCE_TEMPLATE = "graphql/shared/%s.graphqls";
    public static final String GET_CUSTOMER_LIST =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "get_customer_list");
    public static final String GET_CUSTOMER_HISTORY =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "get_customer_history");
    public static final String ME =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "me");
    public static final String CURRENT_USER_FRAGMENT =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "current_user_fragment");
    public static final String REGISTER_ACCOUNT =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "register_account");

    @Autowired
    @Qualifier(TestConfig.ADMIN_CLIENT_BEAN)
    ApiClient adminClient;

    @Autowired
    @Qualifier(TestConfig.SHOP_CLIENT_BEAN)
    ApiClient shopClient;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockDataService mockDataService;

    UserData userData;

    @BeforeAll
    void beforeAll() throws IOException {
        mockDataService.populate(PopulateOptions.builder().customerCount(1).build());
        shopClient.asSuperAdmin();

        userData = new UserData();
        userData.setEmail("test@email.com");
        userData.setFirstName("Cixin");
        userData.setLastName("Liu");
    }

    /**
     * external auth
     */

    Long newCustomerId;

    @Test
    @Order(1)
    public void fails_with_a_bad_token() throws IOException {
        AuthenticationInput authenticationInput = new AuthenticationInput();
        authenticationInput.setMethod(TestAuthenticationStrategy.STRATEGY_NAME);
        authenticationInput.setData(ImmutableMap.of("token", "bad-token"));

        JsonNode inputNode = objectMapper.valueToTree(authenticationInput);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        try {
            shopClient.perform(AUTHENTICATE, variables);
            fail("should have thrown");
        } catch (ApiException apiEx) {
            assertThat(apiEx.getMessage()).isEqualTo("The credentials did not match. Please check and try again");
        }
    }

    @Test
    @Order(2)
    public void creates_a_new_Customer_with_valid_token() throws IOException {
        GraphQLResponse graphQLResponse = this.adminClient.perform(GET_CUSTOMER_LIST, null);
        assertThat(graphQLResponse.isOk());
        CustomerList before = graphQLResponse.get("$.data.customers", CustomerList.class);
        assertThat(before.getTotalItems()).isEqualTo(1);

        this.authenticateCustomer();

        graphQLResponse = this.adminClient.perform(GET_CUSTOMER_LIST, null);
        assertThat(graphQLResponse.isOk());
        CustomerList after = graphQLResponse.get("$.data.customers", CustomerList.class);
        assertThat(after.getTotalItems()).isEqualTo(2);
        assertThat(after.getItems().get(1).getEmailAddress()).isEqualTo(userData.getEmail());

        newCustomerId = after.getItems().get(1).getId();
    }

    @Test
    @Order(3)
    public void creates_customer_history_entry() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", newCustomerId);

        GraphQLResponse graphQLResponse = adminClient.perform(GET_CUSTOMER_HISTORY, variables);
        assertThat(graphQLResponse.isOk());

        Customer customer = graphQLResponse.get("$.data.customer", Customer.class);
        assertThat(customer.getHistory().getItems()).hasSize(2);
        assertThat(customer.getHistory().getTotalItems()).isEqualTo(2);
        HistoryEntry historyEntry0 = customer.getHistory().getItems().get(0);
        assertThat(historyEntry0.getType()).isEqualTo(HistoryEntryType.CUSTOMER_REGISTERED);
        assertThat(historyEntry0.getData())
                .containsOnly(entry(HistoryService.KEY_STRATEGY, TestAuthenticationStrategy.STRATEGY_NAME));
        HistoryEntry historyEntry1 = customer.getHistory().getItems().get(1);
        assertThat(historyEntry1.getType()).isEqualTo(HistoryEntryType.CUSTOMER_VERIFIED);
        assertThat(historyEntry1.getData())
                .containsOnly(entry(HistoryService.KEY_STRATEGY, TestAuthenticationStrategy.STRATEGY_NAME));
    }

    @Test
    @Order(4)
    public void user_authenticationMethod_populated() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", newCustomerId);

        GraphQLResponse graphQLResponse = adminClient.perform(GET_CUSTOMER_USER_AUTH, variables);
        assertThat(graphQLResponse.isOk());

        Customer customer = graphQLResponse.get("$.data.customer", Customer.class);
        assertThat(customer.getUser().getAuthenticationMethods()).hasSize(1);
        assertThat(customer.getUser().getAuthenticationMethods().get(0).getStrategy())
                .isEqualTo(TestAuthenticationStrategy.STRATEGY_NAME);
    }

    @Test
    @Order(5)
    public void creates_authenticated_session() throws IOException {
        GraphQLResponse graphQLResponse = shopClient.perform(ME, null, Arrays.asList(CURRENT_USER_FRAGMENT));
        CurrentUser currentUser = graphQLResponse.get("$.data.me", CurrentUser.class);
        assertThat(currentUser.getIdentifier()).isEqualTo(userData.getEmail());

        shopClient.asAnonymousUser(); // logout
    }

    @Test
    @Order(6)
    public void logging_in_again_reuse_created_user_and_customer() throws IOException {
        this.authenticateCustomer();

        adminClient.asSuperAdmin();
        GraphQLResponse graphQLResponse = this.adminClient.perform(GET_CUSTOMER_LIST, null);
        assertThat(graphQLResponse.isOk());
        CustomerList after = graphQLResponse.get("$.data.customers", CustomerList.class);
        assertThat(after.getTotalItems()).isEqualTo(2);
        assertThat(after.getItems().get(1).getEmailAddress()).isEqualTo(userData.getEmail());
    }

    @Test
    @Order(7)
    public void registerCustomerAccount_with_external_email() throws IOException {
        RegisterCustomerInput input = new RegisterCustomerInput();
        input.setEmailAddress(userData.getEmail());;

        JsonNode inputNode = objectMapper.valueToTree(input);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        GraphQLResponse graphQLResponse = shopClient.perform(REGISTER_ACCOUNT, variables);
        assertThat(graphQLResponse.isOk());
        Boolean result = graphQLResponse.get("$.data.registerCustomerAccount", Boolean.class);
        assertThat(result).isTrue();

        variables = objectMapper.createObjectNode();
        variables.put("id", newCustomerId);

        graphQLResponse = adminClient.perform(GET_CUSTOMER_USER_AUTH, variables);
        assertThat(graphQLResponse.isOk());

        Customer customer = graphQLResponse.get("$.data.customer", Customer.class);
        assertThat(customer.getUser().getAuthenticationMethods()).hasSize(2);
        assertThat(customer.getUser().getAuthenticationMethods().get(1).getStrategy())
                .isEqualTo(Constant.NATIVE_AUTH_STRATEGY_NAME);

        HistoryEntryListOptions options = new HistoryEntryListOptions();
        options.setCurrentPage(2);
        options.setPageSize(2);
        JsonNode optionsNode = objectMapper.valueToTree(options);
        variables = objectMapper.createObjectNode();
        variables.put("id", newCustomerId);
        variables.set("options", optionsNode);

        graphQLResponse = adminClient.perform(GET_CUSTOMER_HISTORY, variables);
        assertThat(graphQLResponse.isOk());

        customer = graphQLResponse.get("$.data.customer", Customer.class);
        assertThat(customer.getHistory().getItems()).hasSize(1);
        assertThat(customer.getHistory().getTotalItems()).isEqualTo(3);
        assertThat(customer.getHistory().getItems().get(0).getType()).isEqualTo(HistoryEntryType.CUSTOMER_REGISTERED);
        assertThat(customer.getHistory().getItems().get(0).getData())
                .containsExactly(entry(HistoryService.KEY_STRATEGY, Constant.NATIVE_AUTH_STRATEGY_NAME));
    }

    private void authenticateCustomer() throws IOException {
        AuthenticationInput authenticationInput = new AuthenticationInput();
        authenticationInput.setMethod(TestAuthenticationStrategy.STRATEGY_NAME);
        authenticationInput.setData(ImmutableMap.of(
                "token", TestAuthenticationStrategy.VALID_AUTH_TOKEN,
                "userData.email", userData.getEmail(),
                "userData.firstName", userData.getFirstName(),
                "userData.lastName", userData.getLastName()));

        JsonNode inputNode = objectMapper.valueToTree(authenticationInput);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        GraphQLResponse graphQLResponse = shopClient.perform(AUTHENTICATE, variables);
        LoginResult loginResult = graphQLResponse.get("$.data.authenticate", LoginResult.class);
        assertThat(loginResult.getUser().getIdentifier()).isEqualTo(userData.getEmail());
    }
}
