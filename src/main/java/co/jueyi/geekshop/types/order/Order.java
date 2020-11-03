package co.jueyi.geekshop.types.order;

import co.jueyi.geekshop.types.common.Adjustment;
import co.jueyi.geekshop.types.common.Node;
import co.jueyi.geekshop.types.customer.Customer;
import co.jueyi.geekshop.types.history.HistoryEntryList;
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
    public Long id;
    public Date createdAt;
    public Date updatedAt;
    /**
     * A unique code for the Order
     */
    public String code;
    public String state;
    /**
     * An order is active as long as the payment process has not been completed
     */
    public Boolean active;
    public Customer customer;
    public OrderAddress shippingAddress;
    public OrderAddress billingAddress;
    public List<OrderLine> lines = new ArrayList<>();
    /**
     * Order-level adjustments to the order total, such as discounts from promotions
     */
    public List<Adjustment> adjustments = new ArrayList<>();
    public List<String> couponCodes = new ArrayList<>();
    /**
     * Promotions applied to the order. Only gets populated after the payment process has completed.
     */
    public List<Promotion> promotions = new ArrayList<>();
    public List<Payment> payments = new ArrayList<>();
    public List<Fulfillment> fulfillments = new ArrayList<>();
    public Integer totalQuantity;
    /**
     * The subTotal is the total of the OrderLines, before order-level promotions and shipping has been applied.
     */
    public Integer subTotal;
    public Integer shipping;
    public ShippingMethod shippingMethod;
    public Integer total;
    public HistoryEntryList history;
}
