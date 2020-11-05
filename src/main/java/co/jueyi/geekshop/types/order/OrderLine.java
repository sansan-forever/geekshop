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
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private ProductVariant productVariant;
    private Asset featuredAsset;
    private Integer unitPrice;
    private Integer quantity;
    private List<OrderItem> items = new ArrayList<>();
    private Integer totalPrice;
    private List<Adjustment> adjustments = new ArrayList<>();
    private Order order;
}
