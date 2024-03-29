/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.entity;

import io.geekshop.types.stock.StockMovementType;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * A StockMovement is created whenever stock of a particular ProductVariant goes in or out.
 *
 * Created on Nov, 2020 by @author bobo
 */
@TableName(value = "tb_stock_movement")
@Data
@EqualsAndHashCode(callSuper = true)
public class StockMovementEntity extends BaseEntity {
    private StockMovementType type;
    private Long productVariantId;
    private Integer quantity;
    private Long orderLineId; // for Sale & Cancellation
    private Long orderItemId; // for Return
}
