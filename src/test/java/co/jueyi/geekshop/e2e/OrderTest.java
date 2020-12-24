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
import co.jueyi.geekshop.types.history.HistoryEntryListOptions;
import co.jueyi.geekshop.types.history.HistoryEntryType;
import co.jueyi.geekshop.types.order.*;
import co.jueyi.geekshop.types.payment.Payment;
import co.jueyi.geekshop.types.product.Product;
import co.jueyi.geekshop.types.product.ProductVariant;
import co.jueyi.geekshop.types.product.UpdateProductVariantInput;
import co.jueyi.geekshop.types.stock.StockMovement;
import co.jueyi.geekshop.types.stock.StockMovementType;
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
import java.util.Objects;
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
    static final String GET_PRODUCT_WITH_VARIANTS =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "get_product_with_variants");
    static final String ASSET_FRAGMENT =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "asset_fragment");
    static final String PRODUCT_VARIANT_FRAGMENT =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "product_variant_fragment");
    static final String PRODUCT_WITH_VARIANTS_FRAGMENT =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "product_with_variants_fragment");
    static final String UPDATE_PRODUCT_VARIANTS =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "update_product_variants");
    static final String GET_STOCK_MOVEMENT =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "get_stock_movement");
    static final String VARIANT_WITH_STOCK_FRAGMENT =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "variant_with_stock_fragment");

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
    static final String GET_ORDER_FULFILLMENTS =
            String.format(ADMIN_ORDER_GRAPHQL_RESOURCE_TEMPLATE, "get_order_fulfillments");
    static final String GET_ORDER_LIST_FULFILLMENTS =
            String.format(ADMIN_ORDER_GRAPHQL_RESOURCE_TEMPLATE, "get_order_list_fulfillments");
    static final String GET_ORDER_FULFILLMENT_ITEMS =
            String.format(ADMIN_ORDER_GRAPHQL_RESOURCE_TEMPLATE, "get_order_fulfillment_items");
    static final String CANCEL_ORDER =
            String.format(ADMIN_ORDER_GRAPHQL_RESOURCE_TEMPLATE, "cancel_order");


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
    public void order_history_contains_expected_entries_01() throws IOException {
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

    @Test
    @org.junit.jupiter.api.Order(10)
    public void creates_a_partial_fulfillment() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", 2L);
        GraphQLResponse graphQLResponse =
                adminClient.perform(GET_ORDER, variables, Arrays.asList(
                        ORDER_WITH_LINES_FRAGMENT, ADJUSTMENT_FRAGMENT, SHIPPING_ADDRESS_FRAGMENT, ORDER_ITEM_FRAGMENT));
        Order order = graphQLResponse.get("$.data.orderByAdmin", Order.class);
        assertThat(order.getState()).isEqualTo(OrderState.PaymentSettled.name());
        List<OrderLine> lines = order.getLines();

        FulfillOrderInput input = new FulfillOrderInput();
        input.setLines(order.getLines().stream().map(l -> {
            OrderLineInput orderLineInput = new OrderLineInput();
            orderLineInput.setOrderLineId(l.getId());
            orderLineInput.setQuantity(1);
            return orderLineInput;
        }).collect(Collectors.toList()));
        input.setMethod("Test1");
        input.setTrackingCode("111");

        JsonNode inputNode = objectMapper.valueToTree(input);
        variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        graphQLResponse = adminClient.perform(CREATE_FULFILLMENT, variables);
        Fulfillment fulfillment = graphQLResponse.get("$.data.fulfillOrder", Fulfillment.class);

        assertThat(fulfillment.getMethod()).isEqualTo("Test1");
        assertThat(fulfillment.getTrackingCode()).isEqualTo("111");
        OrderItem orderItem1 = fulfillment.getOrderItems().get(0);
        assertThat(orderItem1.getId()).isEqualTo(lines.get(0).getItems().get(0).getId());
        OrderItem orderItem2 = fulfillment.getOrderItems().get(1);
        assertThat(orderItem2.getId()).isEqualTo(lines.get(1).getItems().get(0).getId());

        variables = objectMapper.createObjectNode();
        variables.put("id", 2L);
        graphQLResponse =
                adminClient.perform(GET_ORDER, variables, Arrays.asList(
                        ORDER_WITH_LINES_FRAGMENT, ADJUSTMENT_FRAGMENT, SHIPPING_ADDRESS_FRAGMENT, ORDER_ITEM_FRAGMENT));
        order = graphQLResponse.get("$.data.orderByAdmin", Order.class);
        assertThat(order.getState()).isEqualTo(OrderState.PartiallyFulfilled.name());

        assertThat(order.getLines().get(0).getItems().get(0).getFulfillment().getId()).isEqualTo(fulfillment.getId());
        assertThat(order.getLines().get(1).getItems().stream().filter(
                i -> i.getFulfillment() != null && Objects.equals(i.getFulfillment().getId(), fulfillment.getId())
        ).collect(Collectors.toList())).hasSize(1);
        assertThat(order.getLines().get(1).getItems().stream().filter(
                i -> i.getFulfillment() == null
        ).collect(Collectors.toList())).hasSize(2);
    }

    @Test
    @org.junit.jupiter.api.Order(10)
    public void creates_a_second_partial_fulfillment() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", 2L);
        GraphQLResponse graphQLResponse =
                adminClient.perform(GET_ORDER, variables, Arrays.asList(
                        ORDER_WITH_LINES_FRAGMENT, ADJUSTMENT_FRAGMENT, SHIPPING_ADDRESS_FRAGMENT, ORDER_ITEM_FRAGMENT));
        Order order = graphQLResponse.get("$.data.orderByAdmin", Order.class);
        assertThat(order.getState()).isEqualTo(OrderState.PartiallyFulfilled.name());
        List<OrderLine> lines = order.getLines();

        FulfillOrderInput input = new FulfillOrderInput();
        OrderLineInput orderLineInput = new OrderLineInput();
        orderLineInput.setOrderLineId(lines.get(1).getId());
        orderLineInput.setQuantity(1);
        input.getLines().add(orderLineInput);
        input.setMethod("Test2");
        input.setTrackingCode("222");

        JsonNode inputNode = objectMapper.valueToTree(input);
        variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        graphQLResponse = adminClient.perform(CREATE_FULFILLMENT, variables);
        graphQLResponse.get("$.data.fulfillOrder", Fulfillment.class);

        variables = objectMapper.createObjectNode();
        variables.put("id", 2L);
        graphQLResponse =
                adminClient.perform(GET_ORDER, variables, Arrays.asList(
                        ORDER_WITH_LINES_FRAGMENT, ADJUSTMENT_FRAGMENT, SHIPPING_ADDRESS_FRAGMENT, ORDER_ITEM_FRAGMENT));
        order = graphQLResponse.get("$.data.orderByAdmin", Order.class);
        assertThat(order.getState()).isEqualTo(OrderState.PartiallyFulfilled.name());
        assertThat(order.getLines().get(1).getItems().stream().filter(
                i -> i.getFulfillment() != null
        ).collect(Collectors.toList())).hasSize(2);
        assertThat(order.getLines().get(1).getItems().stream().filter(
                i -> i.getFulfillment() == null
        ).collect(Collectors.toList())).hasSize(1);
    }

    @Test
    @org.junit.jupiter.api.Order(11)
    public void throws_if_an_OrderItem_already_part_of_a_Fulfillment() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", 2L);
        GraphQLResponse graphQLResponse =
                adminClient.perform(GET_ORDER, variables, Arrays.asList(
                        ORDER_WITH_LINES_FRAGMENT, ADJUSTMENT_FRAGMENT, SHIPPING_ADDRESS_FRAGMENT, ORDER_ITEM_FRAGMENT));
        Order order = graphQLResponse.get("$.data.orderByAdmin", Order.class);
        assertThat(order.getState()).isEqualTo(OrderState.PartiallyFulfilled.name());

        FulfillOrderInput input = new FulfillOrderInput();
        OrderLineInput orderLineInput = new OrderLineInput();
        orderLineInput.setOrderLineId(order.getLines().get(0).getId());
        orderLineInput.setQuantity(1);
        input.getLines().add(orderLineInput);
        input.setMethod("Test");

        JsonNode inputNode = objectMapper.valueToTree(input);
        variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        try {
            adminClient.perform(CREATE_FULFILLMENT, variables);
        } catch (ApiException apiEx) {
            assertThat(apiEx.getErrorMessage()).isEqualTo(
                    "One or more OrderItems have already been fulfilled"
            );
        }
    }

    @Test
    @org.junit.jupiter.api.Order(12)
    public void completes_fulfillment() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", 2L);
        GraphQLResponse graphQLResponse =
                adminClient.perform(GET_ORDER, variables, Arrays.asList(
                        ORDER_WITH_LINES_FRAGMENT, ADJUSTMENT_FRAGMENT, SHIPPING_ADDRESS_FRAGMENT, ORDER_ITEM_FRAGMENT));
        Order order = graphQLResponse.get("$.data.orderByAdmin", Order.class);
        assertThat(order.getState()).isEqualTo(OrderState.PartiallyFulfilled.name());

        OrderItem unfulfilledItem = order.getLines().get(1).getItems().stream()
                .filter(i -> i.getFulfillment() == null).findFirst().get();

        FulfillOrderInput input = new FulfillOrderInput();
        OrderLineInput orderLineInput = new OrderLineInput();
        orderLineInput.setOrderLineId(order.getLines().get(1).getId());
        orderLineInput.setQuantity(1);
        input.getLines().add(orderLineInput);
        input.setMethod("Test3");
        input.setTrackingCode("333");

        JsonNode inputNode = objectMapper.valueToTree(input);
        variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        graphQLResponse = adminClient.perform(CREATE_FULFILLMENT, variables);
        Fulfillment fulfillment = graphQLResponse.get("$.data.fulfillOrder", Fulfillment.class);
        assertThat(fulfillment.getMethod()).isEqualTo("Test3");
        assertThat(fulfillment.getTrackingCode()).isEqualTo("333");
        assertThat(fulfillment.getOrderItems()).hasSize(1);
        assertThat(fulfillment.getOrderItems().get(0).getId()).isEqualTo(unfulfilledItem.getId());

        variables = objectMapper.createObjectNode();
        variables.put("id", 2L);
        graphQLResponse =
                adminClient.perform(GET_ORDER, variables, Arrays.asList(
                        ORDER_WITH_LINES_FRAGMENT, ADJUSTMENT_FRAGMENT, SHIPPING_ADDRESS_FRAGMENT, ORDER_ITEM_FRAGMENT));
        order = graphQLResponse.get("$.data.orderByAdmin", Order.class);
        assertThat(order.getState()).isEqualTo(OrderState.Fulfilled.name());
    }

    @Test
    @org.junit.jupiter.api.Order(13)
    public void order_history_contains_expected_entries_02() throws IOException {
        HistoryEntryListOptions options = new HistoryEntryListOptions();
        options.setPageSize(5);
        options.setCurrentPage(2);

        JsonNode optionsNode = objectMapper.valueToTree(options);

        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", 2L);
        variables.set("options", optionsNode);

        GraphQLResponse graphQLResponse =
                adminClient.perform(GET_ORDER_HISTORY, variables);
        Order order = graphQLResponse.get("$.data.orderByAdmin", Order.class);
        assertThat(order.getHistory().getItems()).hasSize(5);

        HistoryEntry historyEntry1 = order.getHistory().getItems().get(0);
        assertThat(historyEntry1.getType()).isEqualTo(HistoryEntryType.ORDER_FULFILLMENT);
        assertThat(historyEntry1.getData())
                .containsExactlyInAnyOrderEntriesOf(ImmutableMap.of("fulfillmentId", "1"));

        HistoryEntry historyEntry2 = order.getHistory().getItems().get(1);
        assertThat(historyEntry2.getType()).isEqualTo(HistoryEntryType.ORDER_STATE_TRANSITION);
        assertThat(historyEntry2.getData())
                .containsExactlyInAnyOrderEntriesOf(
                        ImmutableMap.of("from", "PaymentSettled", "to", "PartiallyFulfilled"));

        HistoryEntry historyEntry3 = order.getHistory().getItems().get(2);
        assertThat(historyEntry3.getType()).isEqualTo(HistoryEntryType.ORDER_FULFILLMENT);
        assertThat(historyEntry3.getData())
                .containsExactlyInAnyOrderEntriesOf(
                        ImmutableMap.of("fulfillmentId", "2"));

        HistoryEntry historyEntry4 = order.getHistory().getItems().get(3);
        assertThat(historyEntry4.getType()).isEqualTo(HistoryEntryType.ORDER_STATE_TRANSITION);
        assertThat(historyEntry4.getData())
                .containsExactlyInAnyOrderEntriesOf(
                        ImmutableMap.of("from", "PartiallyFulfilled", "to", "PartiallyFulfilled"));

        HistoryEntry historyEntry5 = order.getHistory().getItems().get(4);
        assertThat(historyEntry5.getType()).isEqualTo(HistoryEntryType.ORDER_FULFILLMENT);
        assertThat(historyEntry5.getData())
                .containsExactlyInAnyOrderEntriesOf(
                        ImmutableMap.of("fulfillmentId", "3"));

        // 还有1个在第3页
        options = new HistoryEntryListOptions();
        options.setPageSize(5);
        options.setCurrentPage(3);

        optionsNode = objectMapper.valueToTree(options);

        variables = objectMapper.createObjectNode();
        variables.put("id", 2L);
        variables.set("options", optionsNode);

        graphQLResponse =
                adminClient.perform(GET_ORDER_HISTORY, variables);
        order = graphQLResponse.get("$.data.orderByAdmin", Order.class);
        assertThat(order.getHistory().getItems()).hasSize(1);

        historyEntry1 = order.getHistory().getItems().get(0);
        assertThat(historyEntry1.getType()).isEqualTo(HistoryEntryType.ORDER_STATE_TRANSITION);
        assertThat(historyEntry1.getData())
                .containsExactlyInAnyOrderEntriesOf(
                        ImmutableMap.of("from", "PartiallyFulfilled", "to", "Fulfilled"));
    }

    @Test
    @org.junit.jupiter.api.Order(14)
    public void order_fulfillments_resolver_for_single_order() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", 2L);

        GraphQLResponse graphQLResponse =
                adminClient.perform(GET_ORDER_FULFILLMENTS, variables);
        Order order = graphQLResponse.get("$.data.orderByAdmin", Order.class);
        assertThat(order.getFulfillments()).hasSize(3);

        Fulfillment fulfillment1 = order.getFulfillments().get(0);
        assertThat(fulfillment1.getId()).isEqualTo(1L);
        assertThat(fulfillment1.getMethod()).isEqualTo("Test1");

        Fulfillment fulfillment2 = order.getFulfillments().get(1);
        assertThat(fulfillment2.getId()).isEqualTo(2L);
        assertThat(fulfillment2.getMethod()).isEqualTo("Test2");

        Fulfillment fulfillment3 = order.getFulfillments().get(2);
        assertThat(fulfillment3.getId()).isEqualTo(3L);
        assertThat(fulfillment3.getMethod()).isEqualTo("Test3");
    }

    @Test
    @org.junit.jupiter.api.Order(15)
    public void order_fulfillments_resolver_for_order_list() throws IOException {
        GraphQLResponse graphQLResponse =
                adminClient.perform(GET_ORDER_LIST_FULFILLMENTS, null);
        OrderList orderList = graphQLResponse.get("$.data.orders", OrderList.class);

        assertThat(orderList.getItems().get(0).getFulfillments()).isEmpty();
        assertThat(orderList.getItems().get(1).getFulfillments()).hasSize(3);
        Fulfillment fulfillment1 = orderList.getItems().get(1).getFulfillments().get(0);
        assertThat(fulfillment1.getId()).isEqualTo(1L);
        assertThat(fulfillment1.getMethod()).isEqualTo("Test1");

        Fulfillment fulfillment2 = orderList.getItems().get(1).getFulfillments().get(1);
        assertThat(fulfillment2.getId()).isEqualTo(2L);
        assertThat(fulfillment2.getMethod()).isEqualTo("Test2");

        Fulfillment fulfillment3 = orderList.getItems().get(1).getFulfillments().get(2);
        assertThat(fulfillment3.getId()).isEqualTo(3L);
        assertThat(fulfillment3.getMethod()).isEqualTo("Test3");
    }

    @Test
    @org.junit.jupiter.api.Order(16)
    public void order_fulfillments_orderItems_resolver() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", 2L);

        GraphQLResponse graphQLResponse =
                adminClient.perform(GET_ORDER_FULFILLMENT_ITEMS, variables);
        Order order = graphQLResponse.get("$.data.orderByAdmin", Order.class);
        assertThat(order.getFulfillments().get(0).getOrderItems()).hasSize(2);
        assertThat(order.getFulfillments().get(0).getOrderItems().stream()
                .map(OrderItem::getId).collect(Collectors.toList())).containsExactly(3L, 4L);
        assertThat(order.getFulfillments().get(1).getOrderItems()).hasSize(1);
        assertThat(order.getFulfillments().get(1).getOrderItems().stream()
                .map(OrderItem::getId).collect(Collectors.toList())).containsExactly(5L);
    }

    /**
     * cancellation by orderId
     */

    @Test
    @org.junit.jupiter.api.Order(17)
    public void cancel_from_AddingItems_state() throws IOException {
        List<Object> testOrderInfoList =
                createTestOrder(adminClient, shopClient, customers.get(0).getEmailAddress(), password);
        Product testProduct = (Product) testOrderInfoList.get(0);
        Long testProductVariantId = (Long) testOrderInfoList.get(1);
        Long testOrderId = (Long) testOrderInfoList.get(2);

        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", testOrderId);

        GraphQLResponse graphQLResponse =
                adminClient.perform(GET_ORDER, variables, Arrays.asList(
                        ORDER_WITH_LINES_FRAGMENT, ADJUSTMENT_FRAGMENT, SHIPPING_ADDRESS_FRAGMENT, ORDER_ITEM_FRAGMENT));
        Order order = graphQLResponse.get("$.data.orderByAdmin", Order.class);

        assertThat(order.getState()).isEqualTo(OrderState.AddingItems.name());

        CancelOrderInput input = new CancelOrderInput();
        input.setOrderId(testOrderId);

        JsonNode inputNode = objectMapper.valueToTree(input);
        variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        adminClient.perform(CANCEL_ORDER, variables);

        variables = objectMapper.createObjectNode();
        variables.put("id", testOrderId);

        graphQLResponse =
                adminClient.perform(GET_ORDER, variables, Arrays.asList(
                        ORDER_WITH_LINES_FRAGMENT, ADJUSTMENT_FRAGMENT, SHIPPING_ADDRESS_FRAGMENT, ORDER_ITEM_FRAGMENT));
        order = graphQLResponse.get("$.data.orderByAdmin", Order.class);

        assertThat(order.getState()).isEqualTo(OrderState.Cancelled.name());
        assertThat(order.getActive()).isFalse();
        assertNoStockMovementsCreated(testProduct.getId());
    }

    private void assertNoStockMovementsCreated(Long productId) throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", productId);

        GraphQLResponse graphQLResponse = adminClient.perform(
                GET_STOCK_MOVEMENT, variables, Arrays.asList(VARIANT_WITH_STOCK_FRAGMENT));
        Product product = graphQLResponse.get("$.data.adminProduct", Product.class);
        ProductVariant productVariant = product.getVariants().get(0);
        assertThat(productVariant.getStockOnHand()).isEqualTo(100);
        assertThat(productVariant.getStockMovements().getItems()).hasSize(1);
        StockMovement stockMovement = productVariant.getStockMovements().getItems().get(0);
        assertThat(stockMovement.getType()).isEqualTo(StockMovementType.ADJUSTMENT);
        assertThat(stockMovement.getQuantity()).isEqualTo(100);
    }

    private List<Object> createTestOrder(
            ApiClient adminClient, ApiClient shopClient, String emailAddress, String password) throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", 3L);

        GraphQLResponse graphQLResponse = adminClient.perform(GET_PRODUCT_WITH_VARIANTS, variables,
                Arrays.asList(PRODUCT_WITH_VARIANTS_FRAGMENT, PRODUCT_VARIANT_FRAGMENT, ASSET_FRAGMENT));
        Product product = graphQLResponse.get("$.data.adminProduct", Product.class);
        Long productVariantId = product.getVariants().get(0).getId();

        // Set the ProductVariant to trackInventory
        UpdateProductVariantInput updateProductVariantInput = new UpdateProductVariantInput();
        updateProductVariantInput.setId(productVariantId);
        updateProductVariantInput.setTrackInventory(true);

        JsonNode inputNode = objectMapper.valueToTree(
                Arrays.asList(updateProductVariantInput));
        variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        graphQLResponse = this.adminClient.perform(UPDATE_PRODUCT_VARIANTS, variables,
                Arrays.asList(PRODUCT_VARIANT_FRAGMENT, ASSET_FRAGMENT));
        List<ProductVariant> updatedProductVariants =
                graphQLResponse.getList("$.data.updateProductVariants", ProductVariant.class);

        // Add the ProductVariant to the Order
        shopClient.asUserWithCredentials(emailAddress, password);

        shopClient.asUserWithCredentials(emailAddress, password);
        variables = objectMapper.createObjectNode();
        variables.put("productVariantId", productVariantId);
        variables.put("quantity", 2);
        graphQLResponse = shopClient.perform(ADD_ITEM_TO_ORDER, variables);
        Order order = graphQLResponse.get("$.data.addItemToOrder", Order.class);
        Long orderId = order.getId();

        return Arrays.asList(product, productVariantId, orderId);
    }
}
