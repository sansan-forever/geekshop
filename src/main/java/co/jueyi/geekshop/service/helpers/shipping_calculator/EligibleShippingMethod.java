/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.service.helpers.shipping_calculator;

import co.jueyi.geekshop.config.shipping_method.ShippingCalculationResult;
import co.jueyi.geekshop.entity.ShippingMethodEntity;
import lombok.Data;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Data
public class EligibleShippingMethod {
    private ShippingMethodEntity method;
    private ShippingCalculationResult result;
}
