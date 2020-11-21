/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.stock;

import co.jueyi.geekshop.types.common.Node;
import co.jueyi.geekshop.types.order.OrderItem;
import co.jueyi.geekshop.types.product.ProductVariant;
import lombok.Data;

import java.util.Date;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class Return implements Node, StockMovement {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private ProductVariant productVariant;
    private Long productVariantId; // 内部使用，GraphQL不可见
    private StockMovementType type;
    private Integer quantity;
    private OrderItem orderItem;
    private Long orderItemId; // 内部使用，GraphQL不可见
}
