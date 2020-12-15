/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.order;

import co.jueyi.geekshop.types.common.Adjustment;
import co.jueyi.geekshop.types.common.Node;
import co.jueyi.geekshop.types.customer.Customer;
import co.jueyi.geekshop.types.history.HistoryEntryList;
import co.jueyi.geekshop.types.history.HistoryEntryListOptions;
import co.jueyi.geekshop.types.payment.Payment;
import co.jueyi.geekshop.types.promotion.Promotion;
import co.jueyi.geekshop.types.shipping.ShippingMethod;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class Order implements Node {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    /**
     * A unique code for the Order
     */
    private String code;
    private String state;
    /**
     * An order is active as long as the payment process has not been completed
     */
    private Boolean active;
    private Customer customer;
    private OrderAddress shippingAddress;
    private OrderAddress billingAddress;
    private List<OrderLine> lines = new ArrayList<>();
    /**
     * Order-level adjustments to the order total, such as discounts from promotions
     */
    private List<Adjustment> adjustments = new ArrayList<>();
    private List<String> couponCodes = new ArrayList<>();
    /**
     * Promotions applied to the order. Only gets populated after the payment process has completed.
     */
    private List<Promotion> promotions = new ArrayList<>();
    private List<Payment> payments = new ArrayList<>();
    private List<Fulfillment> fulfillments = new ArrayList<>();
    private Integer totalQuantity;
    /**
     * The subTotal is the total of the OrderLines, before order-level promotions and shipping_method has been applied.
     */
    private Integer subTotal;
    private Integer shipping;
    private ShippingMethod shippingMethod;
    private Integer total;
    private HistoryEntryList history;

    public HistoryEntryList getHistory(HistoryEntryListOptions options) {
        return null; // TODO
    }

    private List<String> nextStates = new ArrayList<>(); // 该字段仅Admin可见
}
