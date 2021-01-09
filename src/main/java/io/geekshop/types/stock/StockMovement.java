/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.stock;

import io.geekshop.types.common.Node;
import io.geekshop.types.order.OrderItem;
import io.geekshop.types.order.OrderLine;
import io.geekshop.types.product.ProductVariant;
import lombok.Data;

import java.util.Date;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class StockMovement implements Node {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private ProductVariant productVariant;
    private Long productVariantId; // 内部使用字段，GraphQL不可见
    private StockMovementType type;
    private Integer quantity;
    private OrderLine orderLine; // for Sale & Cancellation
    private Long orderLineId; // 内部使用字段，GraphQL不可见
    private OrderItem orderItem; // for Return
    private Long orderItemId; // 内部使用字段，GraphQL不可见
}
