/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.config.shipping_method;

import co.jueyi.geekshop.common.ConfigArgValues;
import co.jueyi.geekshop.entity.OrderEntity;
import co.jueyi.geekshop.types.common.ConfigArgDefinition;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on Dec, 2020 by @author bobo
 */
@SuppressWarnings("Duplicates")
public class DefaultShippingCalculator extends ShippingCalculator {

    private final Map<String, ConfigArgDefinition> argSpec;

    public DefaultShippingCalculator() {
        super(
                "default-shipping-calculator",
                "Default Flat-Rate Shipping Calculator"
        );

        Map<String, ConfigArgDefinition> argDefMap = new HashMap<>();
        ConfigArgDefinition argDef = new ConfigArgDefinition();
        argDef.setType("int");
        argDef.setUi(ImmutableMap.of("component", "currency-form-input"));
        argDef.setLabel("Shipping price");
        argDefMap.put("rate", argDef);

        argSpec = argDefMap;
    }

    @Override
    public ShippingCalculationResult calculate(OrderEntity orderEntity, ConfigArgValues argValues) {
        ShippingCalculationResult result = new ShippingCalculationResult();
        Float rate = argValues.getFloat("rate");
        if (rate == null) {
            result.setPrice(0);
        } else {
            result.setPrice(Math.round(rate));
        }
        return result;
    }

    @Override
    public Map<String, ConfigArgDefinition> getArgSpec() {
        return argSpec;
    }
}
