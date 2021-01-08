/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.service.helpers.shipping_calculator;

import io.geekshop.config.shipping_method.ShippingCalculationResult;
import io.geekshop.entity.ShippingMethodEntity;
import lombok.Data;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Data
public class EligibleShippingMethod {
    private ShippingMethodEntity method;
    private ShippingCalculationResult result;
}
