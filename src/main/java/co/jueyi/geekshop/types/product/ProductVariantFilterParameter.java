/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.product;

import co.jueyi.geekshop.types.common.BooleanOperators;
import co.jueyi.geekshop.types.common.DateOperators;
import co.jueyi.geekshop.types.common.NumberOperators;
import co.jueyi.geekshop.types.common.StringOperators;
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
