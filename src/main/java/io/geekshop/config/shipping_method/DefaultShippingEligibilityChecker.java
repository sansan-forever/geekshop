/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.config.shipping_method;

import io.geekshop.common.ConfigArgValues;
import io.geekshop.entity.OrderEntity;
import io.geekshop.types.common.ConfigArgDefinition;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on Dec, 2020 by @author bobo
 */
@SuppressWarnings("Duplicates")
public class DefaultShippingEligibilityChecker extends ShippingEligibilityChecker {

    private final Map<String, ConfigArgDefinition> argSpec;

    public DefaultShippingEligibilityChecker() {
        super(
                "default-shipping-eligibility-checker",
                "Default Shipping Eligibility Checker"
        );

        Map<String, ConfigArgDefinition> argDefMap = new HashMap<>();
        ConfigArgDefinition argDef = new ConfigArgDefinition();
        argDef.setType("int");
        argDef.setUi(ImmutableMap.of("component", "currency-form-input"));
        argDef.setLabel("Minimum order value");
        argDef.setDescription("Order is eligible only if its total is greater or equal to this value");
        argDefMap.put("orderMinimum", argDef);

        argSpec = argDefMap;
    }

    @Override
    public boolean check(OrderEntity orderEntity, ConfigArgValues argValues) {
        Integer orderMinimum = argValues.getInteger("orderMinimum");
        if (orderMinimum == null) return false;
        return orderEntity.getTotal() >= orderMinimum;
    }

    @Override
    public Map<String, ConfigArgDefinition> getArgSpec() {
        return argSpec;
    }
}
