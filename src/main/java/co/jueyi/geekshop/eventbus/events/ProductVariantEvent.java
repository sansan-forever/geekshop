/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.eventbus.events;

import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.entity.BaseEntity;
import co.jueyi.geekshop.entity.ProductVariantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * This event is fired whenever a {@link co.jueyi.geekshop.types.product.ProductVariant} is added, updated
 * or deleted.
 *
 * Created on Nov, 2020 by @author bobo
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProductVariantEvent extends BaseEntity {
    private final RequestContext ctx;
    private final List<ProductVariantEntity> variants;
    private final String type;
}
