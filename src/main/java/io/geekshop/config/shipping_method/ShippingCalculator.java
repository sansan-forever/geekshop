/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.config.shipping_method;

import io.geekshop.common.ConfigArgValues;
import io.geekshop.common.ConfigurableOperationDef;
import io.geekshop.entity.OrderEntity;
import io.geekshop.types.shipping.ShippingMethod;
import lombok.Getter;

/**
 * The ShippingCalculator is used by a {@link ShippingMethod} to calculate the price of shipping_method on a given
 * {@link OrderEntity}.
 *
 * Created on Dec, 2020 by @author bobo
 */
@Getter
public abstract class ShippingCalculator extends ConfigurableOperationDef {
    private final String code;
    private final String description;

    protected ShippingCalculator(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Calculates the price of shipping_method for the given Order.
     */
    public abstract  ShippingCalculationResult calculate(OrderEntity orderEntity, ConfigArgValues argValues);
}
