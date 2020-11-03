package co.jueyi.geekshop.types.payment;

import co.jueyi.geekshop.types.common.Node;
import co.jueyi.geekshop.types.order.OrderItem;
import lombok.Data;

import java.util.*;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class Refund implements Node {
    public Long id;
    public Date createdAt;
    public Date updatedAt;
    public Integer items;
    public Integer shipping;
    public Integer adjustment;
    public Integer total;
    public String method;
    public String state;
    public String transactionId;
    public String reason;
    public List<OrderItem> orderItems = new ArrayList<>();
    public Long paymentId;
    public Map<String, String> metadata = new HashMap<>();
}
