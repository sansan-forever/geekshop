/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.config.shipping_method;

import co.jueyi.geekshop.common.ConfigArgValues;
import co.jueyi.geekshop.common.ConfigurableOperationDef;
import co.jueyi.geekshop.entity.OrderEntity;
import co.jueyi.geekshop.types.shipping.ShippingMethod;
import lombok.Getter;

/**
 * The ShippingEligibilityChecker class is used to check whether an order qualifies for a given {@link ShippingMethod}.
 *
 * Created on Dec, 2020 by @author bobo
 */
@Getter
public abstract class ShippingEligibilityChecker extends ConfigurableOperationDef {
    private final String code;
    private final String description;

    protected ShippingEligibilityChecker(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Check the given Order to determine whether it is eligible.
     */
    public abstract boolean check(OrderEntity orderEntity, ConfigArgValues argValues);
}
