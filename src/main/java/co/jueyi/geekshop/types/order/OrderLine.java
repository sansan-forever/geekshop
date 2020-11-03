package co.jueyi.geekshop.types.order;

import co.jueyi.geekshop.types.asset.Asset;
import co.jueyi.geekshop.types.common.Adjustment;
import co.jueyi.geekshop.types.common.Node;
import co.jueyi.geekshop.types.product.ProductVariant;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class OrderLine implements Node {
    public Long id;
    public Date createdAt;
    public Date updatedAt;
    public ProductVariant productVariant;
    public Asset featuredAsset;
    public Integer unitPrice;
    public Integer quantity;
    public List<OrderItem> items = new ArrayList<>();
    public Integer totalPrice;
    public List<Adjustment> adjustments = new ArrayList<>();
    public Order order;
}
