/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.eventbus.events;

import io.geekshop.common.RequestContext;
import io.geekshop.entity.BaseEntity;
import io.geekshop.entity.ProductVariantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * This event is fired whenever a {@link io.geekshop.types.product.ProductVariant} is added, updated
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
