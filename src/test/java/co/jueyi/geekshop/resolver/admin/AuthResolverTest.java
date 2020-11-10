package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.*;
import co.jueyi.geekshop.common.Constant;
import co.jueyi.geekshop.types.customer.CustomerList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.GraphQLResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on Nov, 2020 by @author bobo
 */
@GeekShopGraphQLTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
public class AuthResolverTest {

    public static final String SHARED_GRAPHQL_RESOURCE_TEMPLATE = "graphql/shared/%s.graphqls";
    public static final String ADMIN_ME =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "admin_me");
    public static final String CURRENT_USER_FRAGMENT =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "current_user_fragment");
    public static final String ATTEMPT_LOGIN =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "admin_attempt_login");
    public static final String GET_CUSTOMER_LIST =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "get_customer_list");

    @Autowired
    ApiClient apiClient;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockDataService mockDataService;

    @BeforeAll
    void beforeAll() throws IOException {
        mockDataService.populate(PopulateOptions.builder().customerCount(1).build());
    }

    /**
     * admin permissions test suite
     */
    @Test
    @Order(1)
    public void me_is_not_permitted_for_anonymous_user() throws IOException {
        // Anonymous user
        this.apiClient.asAnonymousUser();

        try {
            this.apiClient.perform(ADMIN_ME, null, Arrays.asList(CURRENT_USER_FRAGMENT));
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
                this.apiClient.perform(ATTEMPT_LOGIN, variables, Arrays.asList(CURRENT_USER_FRAGMENT));
        assertThat(graphQLResponse.isOk());
    }

    @Test
    @Order(3)
    public void customer_user_cannot_login() throws IOException {
        this.apiClient.asSuperAdmin();
        GraphQLResponse graphQLResponse = this.apiClient.perform(GET_CUSTOMER_LIST, null);
        assertThat(graphQLResponse.isOk());
        CustomerList customerList = graphQLResponse.get("$.data.customers", CustomerList.class);
        assertThat(customerList.getItems()).hasSize(1);
        String customerEmailAddress = customerList.getItems().get(0).getEmailAddress();

        try {
            this.apiClient.asUserWithCredentials(customerEmailAddress, MockDataService.TEST_PASSWORD, true);
        } catch (ApiException apiEx) {
            assertThat(apiEx.getMessage()).isEqualTo("The credentials did not match. Please check and try again");
        }
    }
}
