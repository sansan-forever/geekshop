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
import co.jueyi.geekshop.config.payment.TestErrorPaymentMethod;
import co.jueyi.geekshop.config.payment.TestFailingPaymentMethod;
import co.jueyi.geekshop.config.payment.TestSuccessfulPaymentMethod;
import co.jueyi.geekshop.config.payment_method.PaymentOptions;
import co.jueyi.geekshop.options.ConfigOptions;
import co.jueyi.geekshop.utils.TestHelper;
import co.jueyi.geekshop.utils.TestOrderUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graphql.spring.boot.test.GraphQLResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on Dec, 2020 by @author bobo
 */
@GeekShopGraphQLTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
public class ShopOrderTest {

    static final String SHOP_GRAPHQL_RESOURCE_TEMPLATE = "graphql/shop/%s.graphqls";
    static final String ADD_ITEM_TO_ORDER  =
            String.format(SHOP_GRAPHQL_RESOURCE_TEMPLATE, "add_item_to_order");
    static final String APPLY_COUPON_CODE  =
            String.format(SHOP_GRAPHQL_RESOURCE_TEMPLATE, "apply_coupon_code");
    static final String TEST_ORDER_FRAGMENT  =
            String.format(SHOP_GRAPHQL_RESOURCE_TEMPLATE, "test_order_fragment");
    static final String GET_ACTIVE_ORDER  =
            String.format(SHOP_GRAPHQL_RESOURCE_TEMPLATE, "get_active_order");

    @Autowired
    TestHelper testHelper;

    @Autowired
    TestOrderUtils testOrderUtils;

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

    @Autowired
    PaymentOptions paymentOptions;

    @Autowired
    ConfigOptions configOptions;

    @TestConfiguration
    static class ContextConfiguration {
        @Bean
        @Primary
        public PaymentOptions testPaymentOptions() {
            return new PaymentOptions(
                    Arrays.asList(
                            new TestSuccessfulPaymentMethod(),
                            new TestFailingPaymentMethod(),
                            new TestErrorPaymentMethod()
                    )
            );
        }
    }

    @BeforeAll
    void beforeAll() throws IOException {
        PopulateOptions populateOptions = PopulateOptions.builder().customerCount(3).build();
        populateOptions.setInitialData(testHelper.getInitialData());
        populateOptions.setProductCsvPath(testHelper.getTestFixture("e2e-products-full.csv"));

        mockDataService.populate(populateOptions);
        adminClient.asSuperAdmin();

        configOptions.getOrderOptions().setOrderItemsLimit(99);
    }


    /**
     * ordering as anonymous user
     */
    Long firstOrderLineId;
    Long createdCustomerId;
    String orderCode;

    @Test
    @org.junit.jupiter.api.Order(1)
    public void addItemToOrder_starts_with_no_session_token() {
        assertThat(shopClient.getAuthToken()).isNull();
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    public void activeOrder_returns_null_before_any_items_have_been_added() throws IOException {
        GraphQLResponse graphQLResponse =
                shopClient.perform(GET_ACTIVE_ORDER, null, Arrays.asList(TEST_ORDER_FRAGMENT));
        co.jueyi.geekshop.types.order.Order activeOrder =
                graphQLResponse.get("$.data.activeOrder", co.jueyi.geekshop.types.order.Order.class);
        assertThat(activeOrder).isNull();
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    public void activeOrder_creates_an_anonymous_session() throws IOException {
        assertThat(shopClient.getAuthToken()).isNotNull();
        assertThat(shopClient.getAuthToken()).isNotBlank();
    }
}
