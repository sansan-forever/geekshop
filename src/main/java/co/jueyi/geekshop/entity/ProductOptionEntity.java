/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * A ProductOption is used to differentiate {@link ProductVariant}s from one another.
 *
 * Created on Nov, 2020 by @author bobo
 */
@TableName(value = "tb_product_option")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductOptionEntity extends BaseEntity {
    private String name;
    private String code;
    private Long groupId;
}
