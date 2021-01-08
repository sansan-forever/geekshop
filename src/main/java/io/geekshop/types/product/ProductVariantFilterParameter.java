/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.types.product;

import io.geekshop.types.common.BooleanOperators;
import io.geekshop.types.common.DateOperators;
import io.geekshop.types.common.NumberOperators;
import io.geekshop.types.common.StringOperators;
import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class ProductVariantFilterParameter {
    private BooleanOperators enabled;
    private NumberOperators stockOnHand;
    private BooleanOperators trackInventory;
    private DateOperators createdAt;
    private DateOperators updatedAt;
    private StringOperators sku;
    private StringOperators name;
    private NumberOperators price;
}
