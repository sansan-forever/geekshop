/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.e2e;

import co.jueyi.geekshop.*;
import co.jueyi.geekshop.config.TestConfig;
import co.jueyi.geekshop.config.payment.FailsToSettlePaymentMethod;
import co.jueyi.geekshop.config.payment.SingleStageRefundablePaymentMethod;
import co.jueyi.geekshop.config.payment.TwoStagePaymentMethod;
import co.jueyi.geekshop.config.payment_method.PaymentMethodHandler;
import co.jueyi.geekshop.config.payment_method.PaymentOptions;
import co.jueyi.geekshop.service.helpers.order_state_machine.OrderState;
import co.jueyi.geekshop.service.helpers.payment_state_machine.PaymentState;
import co.jueyi.geekshop.types.customer.Customer;
import co.jueyi.geekshop.types.customer.CustomerList;
import co.jueyi.geekshop.types.customer.CustomerListOptions;
import co.jueyi.geekshop.types.history.HistoryEntry;
import co.jueyi.geekshop.types.history.HistoryEntryType;
import co.jueyi.geekshop.types.order.FulfillOrderInput;
import co.jueyi.geekshop.types.order.Order;
import co.jueyi.geekshop.types.order.OrderLineInput;
import co.jueyi.geekshop.types.order.OrderList;
import co.jueyi.geekshop.types.payment.Payment;
import co.jueyi.geekshop.utils.TestHelper;
import co.jueyi.geekshop.utils.TestOrderUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;
import com.graphql.spring.boot.test.GraphQLResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;

/**
 * Created on Dec, 2020 by @author bobo
 */
@GeekShopGraphQLTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
public class OrderTest {

    static final String SHARED_GRAPHQL_RESOURCE_TEMPLATE = "graphql/shared/%s.graphqls";
    static final String GET_CUSTOMER_LIST =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "get_customer_list");
    static final String ORDER_FRAGMENT =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "order_fragment");
    static final String GET_ORDER =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "get_order");
    static final String ORDER_WITH_LINES_FRAGMENT =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "order_with_lines_fragment");
    static final String ADJUSTMENT_FRAGMENT =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "adjustment_fragment");
    static final String SHIPPING_ADDRESS_FRAGMENT =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "shipping_address_fragment");
    static final String ORDER_ITEM_FRAGMENT =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "order_item_fragment");

    static final String SHOP_GRAPHQL_RESOURCE_TEMPLATE = "graphql/shop/%s.graphqls";
    static final String ADD_ITEM_TO_ORDER  =
            String.format(SHOP_GRAPHQL_RESOURCE_TEMPLATE, "add_item_to_order");

    static final String ADMIN_ORDER_GRAPHQL_RESOURCE_TEMPLATE = "graphql/admin/order/%s.graphqls";
    static final String GET_ORDER_LIST  =
            String.format(ADMIN_ORDER_GRAPHQL_RESOURCE_TEMPLATE, "get_order_list");
    static final String GET_ORDER_HISTORY  =
            String.format(ADMIN_ORDER_GRAPHQL_RESOURCE_TEMPLATE, "get_order_history");
    static final String SETTLE_PAYMENT =
            String.format(ADMIN_ORDER_GRAPHQL_RESOURCE_TEMPLATE, "settle_payment");
    static final String CREATE_FULFILLMENT =
            String.format(ADMIN_ORDER_GRAPHQL_RESOURCE_TEMPLATE, "create_fulfillment");

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

    PaymentMethodHandler twoStatePaymentMethod;
    PaymentMethodHandler failsToSettlePaymentMethod;
    PaymentMethodHandler singleStageRefundablePaymentMethod;

    @TestConfiguration
    static class ContextConfiguration {

        @Bean
        @Primary
        public PaymentOptions testPaymentOptions() {
            return new PaymentOptions(
                    Arrays.asList(
                            spy(TwoStagePaymentMethod.class),
                            new FailsToSettlePaymentMethod(),
                            new SingleStageRefundablePaymentMethod()
                    )
            );
        }
    }

    List<Customer> customers;
    String password = MockDataService.TEST_PASSWORD;

    @BeforeAll
    void beforeAll() throws IOException {
        PopulateOptions populateOptions = PopulateOptions.builder().customerCount(3).build();
        populateOptions.setInitialData(testHelper.getInitialData());
        populateOptions.setProductCsvPath(testHelper.getTestFixture("e2e-products-full.csv"));

        mockDataService.populate(populateOptions);
        adminClient.asSuperAdmin();

        twoStatePaymentMethod = paymentOptions.getPaymentMethodHandlers().get(0);
        failsToSettlePaymentMethod = paymentOptions.getPaymentMethodHandlers().get(1);
        singleStageRefundablePaymentMethod = paymentOptions.getPaymentMethodHandlers().get(2);

        CustomerListOptions options = new CustomerListOptions();
        options.setCurrentPage(1);
        options.setPageSize(3);

        JsonNode optionsNode = objectMapper.valueToTree(options);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("options", optionsNode);

        GraphQLResponse graphQLResponse = adminClient.perform(
                GET_CUSTOMER_LIST, variables);
        CustomerList customerList = graphQLResponse.get("$.data.customers", CustomerList.class);
        customers = customerList.getItems();

        shopClient.asUserWithCredentials(customers.get(0).getEmailAddress(), password);
        variables = objectMapper.createObjectNode();
        variables.put("productVariantId", 1L);
        variables.put("quantity", 1);
        shopClient.perform(ADD_ITEM_TO_ORDER, variables);

        variables = objectMapper.createObjectNode();
        variables.put("productVariantId", 2L);
        variables.put("quantity", 1);
        shopClient.perform(ADD_ITEM_TO_ORDER, variables);

        shopClient.asUserWithCredentials(customers.get(1).getEmailAddress(), password);
        variables = objectMapper.createObjectNode();
        variables.put("productVariantId", 2L);
        variables.put("quantity", 1);
        shopClient.perform(ADD_ITEM_TO_ORDER, variables);

        variables = objectMapper.createObjectNode();
        variables.put("productVariantId", 3L);
        variables.put("quantity", 3);
        shopClient.perform(ADD_ITEM_TO_ORDER, variables);
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    public void orders() throws IOException {
        GraphQLResponse graphQLResponse =
                adminClient.perform(GET_ORDER_LIST, null, Arrays.asList(ORDER_FRAGMENT));
        OrderList orderList = graphQLResponse.get("$.data.orders", OrderList.class);
        assertThat(orderList.getItems().stream().map(o -> o.getId())).containsExactly(1L, 2L);
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    public void order() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", 2L);

        GraphQLResponse graphQLResponse =
                adminClient.perform(GET_ORDER, variables, Arrays.asList(
                        ORDER_WITH_LINES_FRAGMENT, ADJUSTMENT_FRAGMENT, SHIPPING_ADDRESS_FRAGMENT, ORDER_ITEM_FRAGMENT));
        Order order = graphQLResponse.get("$.data.orderByAdmin", Order.class);
        assertThat(order.getId()).isEqualTo(2);
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    public void order_history_initially_empty() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", 1L);

        GraphQLResponse graphQLResponse =
                adminClient.perform(GET_ORDER_HISTORY, variables);
        Order order = graphQLResponse.get("$.data.orderByAdmin", Order.class);

        assertThat(order.getHistory().getTotalItems()).isEqualTo(0);
        assertThat(order.getHistory().getItems()).isEmpty();
    }

    /**
     * payments
     */
    @Test
    @org.junit.jupiter.api.Order(4)
    public void settlePayment_fails() throws IOException {
        shopClient.asUserWithCredentials(customers.get(0).getEmailAddress(), password);
        testOrderUtils.proceedToArrangingPayment(shopClient);
        Order order = testOrderUtils.addPaymentToOrder(shopClient, failsToSettlePaymentMethod);

        assertThat(order.getState()).isEqualTo(OrderState.PaymentAuthorized.name());

        Payment payment = order.getPayments().get(0);

        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", payment.getId());
        GraphQLResponse graphQLResponse = adminClient.perform(SETTLE_PAYMENT, variables);
        Payment settlePayment = graphQLResponse.get("$.data.settlePayment", Payment.class);
        assertThat(settlePayment.getId()).isEqualTo(payment.getId());
        assertThat(settlePayment.getState()).isEqualTo(PaymentState.Authorized.name());

        variables = objectMapper.createObjectNode();
        variables.put("id", order.getId());
        graphQLResponse =
                adminClient.perform(GET_ORDER, variables, Arrays.asList(
                        ORDER_WITH_LINES_FRAGMENT, ADJUSTMENT_FRAGMENT, SHIPPING_ADDRESS_FRAGMENT, ORDER_ITEM_FRAGMENT));
        order = graphQLResponse.get("$.data.orderByAdmin", Order.class);
        assertThat(order.getState()).isEqualTo(OrderState.PaymentAuthorized.name());
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    public void settlePayment_succeeds_onStateTransitionStart_called() throws IOException {
        Mockito.clearInvocations(twoStatePaymentMethod);

        shopClient.asUserWithCredentials(customers.get(1).getEmailAddress(), password);
        testOrderUtils.proceedToArrangingPayment(shopClient);
        Order order = testOrderUtils.addPaymentToOrder(shopClient, twoStatePaymentMethod);

        assertThat(order.getState()).isEqualTo(OrderState.PaymentAuthorized.name());

        ArgumentCaptor<PaymentState> fromStateCapture = ArgumentCaptor.forClass(PaymentState.class);
        ArgumentCaptor<PaymentState> toStateCapture = ArgumentCaptor.forClass(PaymentState.class);
        Mockito.verify(twoStatePaymentMethod, Mockito.times(1))
                .onStateTransitionStart(fromStateCapture.capture(), toStateCapture.capture(), any());
        assertThat(fromStateCapture.getValue()).isEqualTo(PaymentState.Created);
        assertThat(toStateCapture.getValue()).isEqualTo(PaymentState.Authorized);

        Payment payment = order.getPayments().get(0);

        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", payment.getId());
        GraphQLResponse graphQLResponse = adminClient.perform(SETTLE_PAYMENT, variables);
        Payment settlePayment = graphQLResponse.get("$.data.settlePayment", Payment.class);
        assertThat(settlePayment.getId()).isEqualTo(payment.getId());
        assertThat(settlePayment.getState()).isEqualTo(PaymentState.Settled.name());
        assertThat(settlePayment.getMetadata()).containsExactly(entry("baz", "quux"), entry("moreData", "42"));

        Mockito.verify(twoStatePaymentMethod, Mockito.times(2))
                .onStateTransitionStart(fromStateCapture.capture(), toStateCapture.capture(), any());
        assertThat(fromStateCapture.getValue()).isEqualTo(PaymentState.Authorized);
        assertThat(toStateCapture.getValue()).isEqualTo(PaymentState.Settled);

        variables = objectMapper.createObjectNode();
        variables.put("id", order.getId());
        graphQLResponse =
                adminClient.perform(GET_ORDER, variables, Arrays.asList(
                        ORDER_WITH_LINES_FRAGMENT, ADJUSTMENT_FRAGMENT, SHIPPING_ADDRESS_FRAGMENT, ORDER_ITEM_FRAGMENT));
        order = graphQLResponse.get("$.data.orderByAdmin", Order.class);
        assertThat(order.getState()).isEqualTo(OrderState.PaymentSettled.name());
        assertThat(order.getPayments().get(0).getState()).isEqualTo(PaymentState.Settled.name());
    }

    @Test
    @org.junit.jupiter.api.Order(6)
    public void order_history_contains_expected_entries() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", 2L);

        GraphQLResponse graphQLResponse =
                adminClient.perform(GET_ORDER_HISTORY, variables);
        Order order = graphQLResponse.get("$.data.orderByAdmin", Order.class);
        assertThat(order.getHistory().getItems()).hasSize(5);

        HistoryEntry historyEntry1 = order.getHistory().getItems().get(0);
        assertThat(historyEntry1.getType()).isEqualTo(HistoryEntryType.ORDER_STATE_TRANSITION);
        assertThat(historyEntry1.getData())
                .containsExactly(entry("from", "AddingItems"), entry("to", "ArrangingPayment"));

        HistoryEntry historyEntry2 = order.getHistory().getItems().get(1);
        assertThat(historyEntry2.getType()).isEqualTo(HistoryEntryType.ORDER_PAYMENT_TRANSITION);
        assertThat(historyEntry2.getData())
                .containsExactlyInAnyOrderEntriesOf(ImmutableMap.of(
                        "paymentId", "2", "from", "Created", "to", "Authorized"));

        HistoryEntry historyEntry3 = order.getHistory().getItems().get(2);
        assertThat(historyEntry3.getType()).isEqualTo(HistoryEntryType.ORDER_STATE_TRANSITION);
        assertThat(historyEntry3.getData())
                .containsExactlyInAnyOrderEntriesOf(ImmutableMap.of(
                        "from", "ArrangingPayment", "to", "PaymentAuthorized"));

        HistoryEntry historyEntry4 = order.getHistory().getItems().get(3);
        assertThat(historyEntry4.getType()).isEqualTo(HistoryEntryType.ORDER_PAYMENT_TRANSITION);
        assertThat(historyEntry4.getData())
                .containsExactlyInAnyOrderEntriesOf(ImmutableMap.of(
                        "paymentId", "2", "from", "Authorized", "to", "Settled"));

        HistoryEntry historyEntry5 = order.getHistory().getItems().get(4);
        assertThat(historyEntry5.getType()).isEqualTo(HistoryEntryType.ORDER_STATE_TRANSITION);
        assertThat(historyEntry5.getData())
                .containsExactlyInAnyOrderEntriesOf(ImmutableMap.of(
                        "from", "PaymentAuthorized", "to", "PaymentSettled"));
    }

    /**
     * fulfillment
     */

    @Test
    @org.junit.jupiter.api.Order(7)
    public void throws_if_Order_is_not_in_PaymentSettled_state() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", 1L);
        GraphQLResponse graphQLResponse =
                adminClient.perform(GET_ORDER, variables, Arrays.asList(
                        ORDER_WITH_LINES_FRAGMENT, ADJUSTMENT_FRAGMENT, SHIPPING_ADDRESS_FRAGMENT, ORDER_ITEM_FRAGMENT));
        Order order = graphQLResponse.get("$.data.orderByAdmin", Order.class);
        assertThat(order.getState()).isEqualTo(OrderState.PaymentAuthorized.name());

        FulfillOrderInput input = new FulfillOrderInput();
        input.setLines(order.getLines().stream().map(l -> {
            OrderLineInput orderLineInput = new OrderLineInput();
            orderLineInput.setOrderLineId(l.getId());
            orderLineInput.setQuantity(l.getQuantity());
            return orderLineInput;
        }).collect(Collectors.toList()));
        input.setMethod("Test");

        JsonNode inputNode = objectMapper.valueToTree(input);
        variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        try {
            adminClient.perform(CREATE_FULFILLMENT, variables);
        } catch (ApiException apiEx) {
            assertThat(apiEx.getErrorMessage()).isEqualTo(
                    "One or more OrderItems belong to an Order which is in an invalid state"
            );
        }
    }

    @Test
    @org.junit.jupiter.api.Order(8)
    public void throws_if_lines_is_empty() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", 2L);
        GraphQLResponse graphQLResponse =
                adminClient.perform(GET_ORDER, variables, Arrays.asList(
                        ORDER_WITH_LINES_FRAGMENT, ADJUSTMENT_FRAGMENT, SHIPPING_ADDRESS_FRAGMENT, ORDER_ITEM_FRAGMENT));
        Order order = graphQLResponse.get("$.data.orderByAdmin", Order.class);
        assertThat(order.getState()).isEqualTo(OrderState.PaymentSettled.name());

        FulfillOrderInput input = new FulfillOrderInput();
        input.setMethod("Test");

        JsonNode inputNode = objectMapper.valueToTree(input);
        variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        try {
            adminClient.perform(CREATE_FULFILLMENT, variables);
        } catch (ApiException apiEx) {
            assertThat(apiEx.getErrorMessage()).isEqualTo(
                    "Nothing to fulfill"
            );
        }
    }

    @Test
    @org.junit.jupiter.api.Order(9)
    public void throws_if_all_quantities_are_zero() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", 2L);
        GraphQLResponse graphQLResponse =
                adminClient.perform(GET_ORDER, variables, Arrays.asList(
                        ORDER_WITH_LINES_FRAGMENT, ADJUSTMENT_FRAGMENT, SHIPPING_ADDRESS_FRAGMENT, ORDER_ITEM_FRAGMENT));
        Order order = graphQLResponse.get("$.data.orderByAdmin", Order.class);
        assertThat(order.getState()).isEqualTo(OrderState.PaymentSettled.name());

        FulfillOrderInput input = new FulfillOrderInput();
        input.setLines(order.getLines().stream().map(l -> {
            OrderLineInput orderLineInput = new OrderLineInput();
            orderLineInput.setOrderLineId(l.getId());
            orderLineInput.setQuantity(0);
            return orderLineInput;
        }).collect(Collectors.toList()));
        input.setMethod("Test");

        JsonNode inputNode = objectMapper.valueToTree(input);
        variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        try {
            adminClient.perform(CREATE_FULFILLMENT, variables);
        } catch (ApiException apiEx) {
            assertThat(apiEx.getErrorMessage()).isEqualTo(
                    "Nothing to fulfill"
            );
        }
    }
}
