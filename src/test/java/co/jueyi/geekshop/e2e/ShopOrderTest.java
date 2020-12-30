/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.e2e;

import co.jueyi.geekshop.*;
import co.jueyi.geekshop.config.TestConfig;
import co.jueyi.geekshop.config.payment.TestErrorPaymentMethod;
import co.jueyi.geekshop.config.payment.TestFailingPaymentMethod;
import co.jueyi.geekshop.config.payment.TestSuccessfulPaymentMethod;
import co.jueyi.geekshop.config.payment_method.PaymentOptions;
import co.jueyi.geekshop.options.ConfigOptions;
import co.jueyi.geekshop.service.helpers.order_state_machine.OrderState;
import co.jueyi.geekshop.types.address.Address;
import co.jueyi.geekshop.types.common.CreateAddressInput;
import co.jueyi.geekshop.types.common.CreateCustomerInput;
import co.jueyi.geekshop.types.customer.Customer;
import co.jueyi.geekshop.types.customer.CustomerList;
import co.jueyi.geekshop.types.customer.CustomerListOptions;
import co.jueyi.geekshop.types.order.Order;
import co.jueyi.geekshop.types.order.OrderAddress;
import co.jueyi.geekshop.types.payment.Payment;
import co.jueyi.geekshop.types.payment.PaymentInput;
import co.jueyi.geekshop.utils.TestHelper;
import co.jueyi.geekshop.utils.TestOrderUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.GraphQLResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Test;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

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
    static final String ADJUST_ITEM_QUANTITY  =
            String.format(SHOP_GRAPHQL_RESOURCE_TEMPLATE, "adjust_item_quantity");
    static final String REMOVE_ITEM_FROM_ORDER  =
            String.format(SHOP_GRAPHQL_RESOURCE_TEMPLATE, "remove_item_from_order");
    static final String GET_NEXT_STATES =
            String.format(SHOP_GRAPHQL_RESOURCE_TEMPLATE, "get_next_states");
    static final String TRANSITION_TO_STATE  =
            String.format(SHOP_GRAPHQL_RESOURCE_TEMPLATE, "transition_to_state");
    static final String SET_CUSTOMER  =
            String.format(SHOP_GRAPHQL_RESOURCE_TEMPLATE, "set_customer");
    static final String SET_SHIPPING_ADDRESS  =
            String.format(SHOP_GRAPHQL_RESOURCE_TEMPLATE, "set_shipping_address");
    static final String SET_BILLING_ADDRESS  =
            String.format(SHOP_GRAPHQL_RESOURCE_TEMPLATE, "set_billing_address");
    static final String ADD_PAYMENT  =
            String.format(SHOP_GRAPHQL_RESOURCE_TEMPLATE, "add_payment");

    static final String SHARED_GRAPHQL_RESOURCE_TEMPLATE = "graphql/shared/%s.graphqls";
    static final String GET_CUSTOMER =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "get_customer");
    static final String CUSTOMER_FRAGMENT =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "customer_fragment");
    static final String ADDRESS_FRAGMENT =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "address_fragment");
    static final String GET_CUSTOMER_LIST =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "get_customer_list");

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

    TestSuccessfulPaymentMethod testSuccessfulPaymentMethod;

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
        testSuccessfulPaymentMethod = (TestSuccessfulPaymentMethod) paymentOptions.getPaymentMethodHandlers().get(0);
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

    @Test
    @org.junit.jupiter.api.Order(4)
    public void addItemToOrder_creates_a_new_Order_with_an_item() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("productVariantId", 1L);
        variables.put("quantity", 1);
        GraphQLResponse graphQLResponse = shopClient.perform(ADD_ITEM_TO_ORDER, variables);

        Order order = graphQLResponse.get("$.data.addItemToOrder", Order.class);
        assertThat(order.getLines().get(0).getQuantity()).isEqualTo(1L);
        assertThat(order.getLines().get(0).getProductVariant().getId()).isEqualTo(1L);
        assertThat(order.getLines().get(0).getId()).isEqualTo(1L);
        firstOrderLineId = order.getLines().get(0).getId();
        orderCode = order.getCode();
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    public void addItemToOrder_errors_with_an_invalid_productVariantId() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("productVariantId", 999L);
        variables.put("quantity", 1);
        try {
            shopClient.perform(ADD_ITEM_TO_ORDER, variables);
            fail("should have thrown");
        } catch (ApiException apiEx) {
            assertThat(apiEx.getErrorMessage()).isEqualTo(
                    "No ProductVariant with the id '999' could be found"
            );
        }
    }

    @Test
    @org.junit.jupiter.api.Order(6)
    public void addItemToOrder_errors_with_a_negative_quantity() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("productVariantId", 999L);
        variables.put("quantity", -3);
        try {
            shopClient.perform(ADD_ITEM_TO_ORDER, variables);
            fail("should have thrown");
        } catch (ApiException apiEx) {
            assertThat(apiEx.getErrorMessage()).isEqualTo(
                    "{ -3 } is not a valid quantity for an OrderItem"
            );
        }
    }

    @Test
    @org.junit.jupiter.api.Order(7)
    public void addItemToOrder_with_an_existing_productVariantId_adds_quantity_to_the_existing_OrderLine()
            throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("productVariantId", 1L);
        variables.put("quantity", 2);
        GraphQLResponse graphQLResponse = shopClient.perform(ADD_ITEM_TO_ORDER, variables);

        Order order = graphQLResponse.get("$.data.addItemToOrder", Order.class);
        assertThat(order.getLines()).hasSize(1);
        assertThat(order.getLines().get(0).getQuantity()).isEqualTo(3);
    }

    @Test
    @org.junit.jupiter.api.Order(8)
    public void addItemToOrder_errors_when_going_beyond_orderItemsLimit() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("productVariantId", 1L);
        variables.put("quantity", 100);
        try {
            shopClient.perform(ADD_ITEM_TO_ORDER, variables);
            fail("should have thrown");
        } catch (ApiException apiEx) {
            assertThat(apiEx.getErrorMessage()).isEqualTo(
                    "Cannot add items. An order may consist of a maximum of { 99 } items"
            );
        }
    }

    @Test
    @org.junit.jupiter.api.Order(9)
    public void adjustOrderLine_adjusts_the_quantity() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("orderLineId", firstOrderLineId);
        variables.put("quantity", 50);
        GraphQLResponse graphQLResponse = shopClient.perform(ADJUST_ITEM_QUANTITY, variables, Arrays.asList(TEST_ORDER_FRAGMENT));
        Order order = graphQLResponse.get("$.data.adjustOrderLine", Order.class);

        assertThat(order.getLines()).hasSize(1);
        assertThat(order.getLines().get(0).getQuantity()).isEqualTo(50);
    }

    @Test
    @org.junit.jupiter.api.Order(10)
    public void adjustOrderLine_with_quantity_0_removes_the_line() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("productVariantId", 3L);
        variables.put("quantity", 2);
        GraphQLResponse graphQLResponse = shopClient.perform(ADD_ITEM_TO_ORDER, variables);

        Order order = graphQLResponse.get("$.data.addItemToOrder", Order.class);

        assertThat(order.getLines()).hasSize(2);
        assertThat(order.getLines().stream().map(i -> i.getProductVariant().getId()).collect(Collectors.toList()))
                .containsExactly(1L, 3L);

        variables = objectMapper.createObjectNode();
        variables.put("orderLineId", order.getLines().get(1).getId());
        variables.put("quantity", 0);
        graphQLResponse = shopClient.perform(ADJUST_ITEM_QUANTITY, variables, Arrays.asList(TEST_ORDER_FRAGMENT));
        order = graphQLResponse.get("$.data.adjustOrderLine", Order.class);

        assertThat(order.getLines()).hasSize(1);
        assertThat(order.getLines().stream().map(i -> i.getProductVariant().getId()).collect(Collectors.toList()))
                .containsExactly(1L);
    }

    @Test
    @org.junit.jupiter.api.Order(11)
    public void adjustOrderLine_errors_when_going_beyond_orderItemsLimit() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("orderLineId", firstOrderLineId);
        variables.put("quantity", 100);
        try {
            shopClient.perform(ADJUST_ITEM_QUANTITY, variables, Arrays.asList(TEST_ORDER_FRAGMENT));
            fail("should have thrown");
        } catch (ApiException apiEx) {
            assertThat(apiEx.getMessage()).isEqualTo(
                    "Cannot add items. An order may consist of a maximum of { 99 } items"
            );
        }
    }

    @Test
    @org.junit.jupiter.api.Order(12)
    public void adjustOrderLine_errors_with_a_negative_quantity() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("orderLineId", firstOrderLineId);
        variables.put("quantity", -3);
        try {
            shopClient.perform(ADJUST_ITEM_QUANTITY, variables, Arrays.asList(TEST_ORDER_FRAGMENT));
            fail("should have thrown");
        } catch (ApiException apiEx) {
            assertThat(apiEx.getMessage()).isEqualTo(
                    "{ -3 } is not a valid quantity for an OrderItem"
            );
        }
    }

    @Test
    @org.junit.jupiter.api.Order(13)
    public void adjustOrderLine_errors_with_an_invalid_orderLineId() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("orderLineId", 999L);
        variables.put("quantity", 5);
        try {
            shopClient.perform(ADJUST_ITEM_QUANTITY, variables, Arrays.asList(TEST_ORDER_FRAGMENT));
            fail("should have thrown");
        } catch (ApiException apiEx) {
            assertThat(apiEx.getMessage()).isEqualTo(
                    "This order does not contain an OrderLine with the id { 999 }"
            );
        }
    }

    @Test
    @org.junit.jupiter.api.Order(14)
    public void removeItemFromOrder_removes_the_correct_item() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("productVariantId", 3L);
        variables.put("quantity", 3);
        GraphQLResponse graphQLResponse = shopClient.perform(ADD_ITEM_TO_ORDER, variables);

        Order order = graphQLResponse.get("$.data.addItemToOrder", Order.class);

        assertThat(order.getLines()).hasSize(2);
        assertThat(order.getLines().stream().map(i -> i.getProductVariant().getId()).collect(Collectors.toList()))
                .containsExactly(1L, 3L);

        variables = objectMapper.createObjectNode();
        variables.put("orderLineId", firstOrderLineId);
        graphQLResponse = shopClient.perform(REMOVE_ITEM_FROM_ORDER, variables, Arrays.asList(TEST_ORDER_FRAGMENT));
        order = graphQLResponse.get("$.data.removeOrderLine", Order.class);
        assertThat(order.getLines()).hasSize(1);
        assertThat(order.getLines().stream().map(i -> i.getProductVariant().getId()).collect(Collectors.toList()))
                .containsExactly(3L);
    }

    @Test
    @org.junit.jupiter.api.Order(15)
    public void removeItemFromOrder_errors_with_an_invalid_orderItemId() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("orderLineId", 999L);
        try {
            shopClient.perform(REMOVE_ITEM_FROM_ORDER, variables, Arrays.asList(TEST_ORDER_FRAGMENT));
            fail("should have thrown");
        } catch (ApiException apiEx) {
            assertThat(apiEx.getErrorMessage()).isEqualTo(
              "This order does not contain an OrderLine with the id { 999 }"
            );
        }
    }

    @Test
    @org.junit.jupiter.api.Order(16)
    public void nextOrderStates_returns_next_valid_states() throws IOException {
        GraphQLResponse graphQLResponse = shopClient.perform(GET_NEXT_STATES, null);
        List<String> states = graphQLResponse.getList("$.data.nextOrderStates", String.class);
        assertThat(states).containsExactly("ArrangingPayment", "Cancelled");
    }

    @Test
    @org.junit.jupiter.api.Order(17)
    public void transitionOrderToState_throws_for_an_invalid_state() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("state", "Completed");
        try {
            shopClient.perform(TRANSITION_TO_STATE, variables);
            fail("should have thrown");
        } catch (ApiException apiEx) {
            assertThat(apiEx.getErrorMessage()).isEqualTo(
                    "No enum constant co.jueyi.geekshop.service.helpers.order_state_machine.OrderState.Completed"
            );
        }
    }

    @Test
    @org.junit.jupiter.api.Order(18)
    public void attempting_to_transition_to_ArrangingPayment_throws_when_Order_has_no_Customer() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("state", "ArrangingPayment");
        try {
            shopClient.perform(TRANSITION_TO_STATE, variables);
            fail("should have thrown");
        } catch (ApiException apiEx) {
            assertThat(apiEx.getErrorMessage()).isEqualTo(
                    "Cannot transition Order to the \"ArrangingPayment\" state without Customer details"
            );
        }
    }

    @Test
    @org.junit.jupiter.api.Order(19)
    public void setCustomerForOrder_creates_a_new_Customer_and_associates_it_with_the_Order() throws IOException {
        CreateCustomerInput input = new CreateCustomerInput();
        input.setEmailAddress("test@test.com");
        input.setFirstName("Test");
        input.setLastName("Person");

        JsonNode inputNode = objectMapper.valueToTree(input);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        GraphQLResponse graphQLResponse = shopClient.perform(SET_CUSTOMER, variables);
        Order order = graphQLResponse.get("$.data.setCustomerForOrder", Order.class);

        Customer customer = order.getCustomer();
        assertThat(customer.getFirstName()).isEqualTo("Test");
        assertThat(customer.getLastName()).isEqualTo("Person");
        assertThat(customer.getEmailAddress()).isEqualTo("test@test.com");
        createdCustomerId = customer.getId();
    }

    @Test
    @org.junit.jupiter.api.Order(20)
    public void setCustomerForOrder_updates_the_existing_customer_if_Customer_already_set() throws IOException {
        CreateCustomerInput input = new CreateCustomerInput();
        input.setEmailAddress("test@test.com");
        input.setFirstName("Changed");
        input.setLastName("Person");

        JsonNode inputNode = objectMapper.valueToTree(input);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        GraphQLResponse graphQLResponse = shopClient.perform(SET_CUSTOMER, variables);
        Order order = graphQLResponse.get("$.data.setCustomerForOrder", Order.class);
        Customer customer = order.getCustomer();
        assertThat(customer.getFirstName()).isEqualTo("Changed");
        assertThat(customer.getLastName()).isEqualTo("Person");
        assertThat(customer.getEmailAddress()).isEqualTo("test@test.com");
        assertThat(customer.getId()).isEqualTo(createdCustomerId);
    }

    @Test
    @org.junit.jupiter.api.Order(21)
    public void setOrderShippingAddress_sets_shipping_address() throws IOException {
        CreateAddressInput input = new CreateAddressInput();
        input.setFullName("name");
        input.setCompany("company");
        input.setStreetLine1("12 the street");
        input.setStreetLine2(null);
        input.setCity("foo");
        input.setProvince("bar");
        input.setPostalCode("123456");
        input.setPhoneNumber("4444444");

        JsonNode inputNode = objectMapper.valueToTree(input);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        GraphQLResponse graphQLResponse = shopClient.perform(SET_SHIPPING_ADDRESS, variables);
        Order order = graphQLResponse.get("$.data.setOrderShippingAddress", Order.class);

        OrderAddress orderAddress = order.getShippingAddress();
        assertThat(orderAddress.getFullName()).isEqualTo("name");
        assertThat(orderAddress.getCompany()).isEqualTo("company");
        assertThat(orderAddress.getStreetLine1()).isEqualTo("12 the street");
        assertThat(orderAddress.getStreetLine2()).isNull();
        assertThat(orderAddress.getCity()).isEqualTo("foo");
        assertThat(orderAddress.getProvince()).isEqualTo("bar");
        assertThat(orderAddress.getPostalCode()).isEqualTo("123456");
        assertThat(orderAddress.getPhoneNumber()).isEqualTo("4444444");
    }

    @Test
    @org.junit.jupiter.api.Order(22)
    public void setOrderBillingAddress_sets_billing_address() throws IOException {
        CreateAddressInput input = new CreateAddressInput();
        input.setFullName("name");
        input.setCompany("company");
        input.setStreetLine1("12 the street");
        input.setStreetLine2(null);
        input.setCity("foo");
        input.setProvince("bar");
        input.setPostalCode("123456");
        input.setPhoneNumber("4444444");

        JsonNode inputNode = objectMapper.valueToTree(input);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        GraphQLResponse graphQLResponse = shopClient.perform(SET_BILLING_ADDRESS, variables);
        Order order = graphQLResponse.get("$.data.setOrderBillingAddress", Order.class);

        OrderAddress orderAddress = order.getBillingAddress();
        assertThat(orderAddress.getFullName()).isEqualTo("name");
        assertThat(orderAddress.getCompany()).isEqualTo("company");
        assertThat(orderAddress.getStreetLine1()).isEqualTo("12 the street");
        assertThat(orderAddress.getStreetLine2()).isNull();
        assertThat(orderAddress.getCity()).isEqualTo("foo");
        assertThat(orderAddress.getProvince()).isEqualTo("bar");
        assertThat(orderAddress.getPostalCode()).isEqualTo("123456");
        assertThat(orderAddress.getPhoneNumber()).isEqualTo("4444444");
    }

    @Test
    @org.junit.jupiter.api.Order(23)
    public void customer_default_Addresses_are_not_updated_before_payment() throws IOException {
        GraphQLResponse graphQLResponse =
                shopClient.perform(GET_ACTIVE_ORDER, null, Arrays.asList(TEST_ORDER_FRAGMENT));
        Order activeOrder = graphQLResponse.get("$.data.activeOrder", Order.class);

        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", activeOrder.getCustomer().getId());

        graphQLResponse =
                adminClient.perform(GET_CUSTOMER, variables, Arrays.asList(CUSTOMER_FRAGMENT, ADDRESS_FRAGMENT));
        Customer customer = graphQLResponse.get("$.data.customer", Customer.class);
        assertThat(customer.getAddresses()).isEmpty();
    }

    @Test
    @org.junit.jupiter.api.Order(24)
    public void can_transition_to_ArrangingPayment_once_Customer_has_been_set() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("state", "ArrangingPayment");
        GraphQLResponse graphQLResponse = shopClient.perform(TRANSITION_TO_STATE, variables);
        Order order = graphQLResponse.get("$.data.transitionOrderToState", Order.class);
        assertThat(order.getId()).isEqualTo(1L);
        assertThat(order.getState()).isEqualTo(OrderState.ArrangingPayment.name());
    }

    @Test
    @org.junit.jupiter.api.Order(25)
    public void adds_a_successful_payment_and_transitions_Order_state() throws IOException {
        PaymentInput input = new PaymentInput();
        input.setMethod(testSuccessfulPaymentMethod.getCode());

        JsonNode inputNode = objectMapper.valueToTree(input);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        GraphQLResponse graphQLResponse =
                shopClient.perform(ADD_PAYMENT, variables, Arrays.asList(TEST_ORDER_FRAGMENT));
        Order order = graphQLResponse.get("$.data.addPaymentToOrder", Order.class);

        Payment payment = order.getPayments().get(0);
        assertThat(order.getState()).isEqualTo("PaymentSettled");
        assertThat(order.getActive()).isFalse();
        assertThat(order.getPayments()).hasSize(1);
        assertThat(payment.getMethod()).isEqualTo(testSuccessfulPaymentMethod.getCode());
        assertThat(payment.getState()).isEqualTo("Settled");
    }

    @Test
    @org.junit.jupiter.api.Order(26)
    public void activeOrder_is_null_after_payment() throws IOException {
        GraphQLResponse graphQLResponse =
                shopClient.perform(GET_ACTIVE_ORDER, null, Arrays.asList(TEST_ORDER_FRAGMENT));
        Order activeOrder = graphQLResponse.get("$.data.activeOrder", Order.class);
        assertThat(activeOrder).isNull();
    }

    @Test
    @org.junit.jupiter.api.Order(27)
    public void customer_default_Addresses_are_updated_after_payment() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", createdCustomerId);

        GraphQLResponse graphQLResponse =
                adminClient.perform(GET_CUSTOMER, variables, Arrays.asList(CUSTOMER_FRAGMENT, ADDRESS_FRAGMENT));
        Customer customer = graphQLResponse.get("$.data.customer", Customer.class);

        Address address = customer.getAddresses().get(0);
        assertThat(address.getStreetLine1()).isEqualTo("12 the street");
        assertThat(address.getPostalCode()).isEqualTo("123456");
        assertThat(address.getDefaultBillingAddress()).isTrue();
        assertThat(address.getDefaultShippingAddress()).isTrue();
    }

    Order activeOrder;
    String authenticatedUserEmailAddress;
    List<Customer> customers;
    String password = MockDataService.TEST_PASSWORD;

    private void before_test_28() throws IOException {
        adminClient.asSuperAdmin();

        CustomerListOptions options = new CustomerListOptions();
        options.setCurrentPage(1);
        options.setPageSize(2);

        JsonNode optionsNode = objectMapper.valueToTree(options);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("options", optionsNode);

        GraphQLResponse graphQLResponse = adminClient.perform(
                GET_CUSTOMER_LIST, variables);
        CustomerList customerList = graphQLResponse.get("$.data.customers", CustomerList.class);
        customers = customerList.getItems();
        authenticatedUserEmailAddress = customers.get(0).getEmailAddress();
        shopClient.asUserWithCredentials(authenticatedUserEmailAddress, password);
    }

    @Test
    @org.junit.jupiter.api.Order(28)
    public void activeOrder_returns_null_before_any_item_have_been_added() throws IOException {
        before_test_28();

        GraphQLResponse graphQLResponse =
                shopClient.perform(GET_ACTIVE_ORDER, null, Arrays.asList(TEST_ORDER_FRAGMENT));
        Order activeOrder = graphQLResponse.get("$.data.activeOrder", Order.class);

        assertThat(activeOrder).isNull();
    }

    @Test
    @org.junit.jupiter.api.Order(29)
    public void addItemToOrder_creates_a_new_Order_with_an_item_02() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("productVariantId", 1L);
        variables.put("quantity", 1);
        GraphQLResponse graphQLResponse = shopClient.perform(ADD_ITEM_TO_ORDER, variables);

        Order order = graphQLResponse.get("$.data.addItemToOrder", Order.class);
        assertThat(order.getLines()).hasSize(1);
        assertThat(order.getLines().get(0).getQuantity()).isEqualTo(1L);
        assertThat(order.getLines().get(0).getProductVariant().getId()).isEqualTo(1L);

        activeOrder = order;
        firstOrderLineId = order.getLines().get(0).getId();
    }

    @Test
    @org.junit.jupiter.api.Order(30)
    public void activeOrder_returns_order_after_item_has_been_added() throws IOException {
        GraphQLResponse graphQLResponse =
                shopClient.perform(GET_ACTIVE_ORDER, null, Arrays.asList(TEST_ORDER_FRAGMENT));
        Order activeOrder = graphQLResponse.get("$.data.activeOrder", Order.class);

        assertThat(activeOrder.getId()).isEqualTo(activeOrder.getId());
        assertThat(activeOrder.getState()).isEqualTo(OrderState.AddingItems.name());
    }
}
