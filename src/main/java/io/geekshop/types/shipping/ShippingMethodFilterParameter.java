/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.types.shipping;

import io.geekshop.types.common.DateOperators;
import io.geekshop.types.common.StringOperators;
import lombok.Data;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Data
public class ShippingMethodFilterParameter {
    private DateOperators createdAt;
    private DateOperators updatedAt;
    private StringOperators code;
    private StringOperators description;
}
