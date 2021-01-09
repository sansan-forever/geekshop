/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created on Dec, 2020 by @author bobo
 */
@TableName(value = "tb_order_promotion_join")
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderPromotionJoinEntity extends BaseEntity {
    private Long orderId;
    private Long promotionId;
}
