package co.jueyi.geekshop.types.order;

import co.jueyi.geekshop.types.common.Node;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class Fulfillment implements Node {
    public Long id;
    public Date createdAt;
    public Date updatedAt;
    public List<OrderItem> orderItems = new ArrayList<>();
    public String method;
    public String trackingCode;
}
