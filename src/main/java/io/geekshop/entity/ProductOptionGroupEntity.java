/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * A grouping of one or more {@link io.geekshop.types.product.ProductOption}
 *
 * Created on Nov, 2020 by @author bobo
 */
@TableName(value = "tb_product_option_group")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductOptionGroupEntity extends BaseEntity {
    private String name;
    private String code;
//    private Long productId;
}
