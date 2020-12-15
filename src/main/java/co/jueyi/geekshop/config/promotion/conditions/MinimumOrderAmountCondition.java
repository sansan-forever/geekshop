/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.config.promotion.conditions;

import co.jueyi.geekshop.common.ConfigArgValues;
import co.jueyi.geekshop.config.promotion.PromotionCondition;
import co.jueyi.geekshop.entity.OrderEntity;
import co.jueyi.geekshop.types.common.ConfigArgDefinition;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on Dec, 2020 by @author bobo
 */
@SuppressWarnings("Duplicates")
public class MinimumOrderAmountCondition extends PromotionCondition {
    private final Map<String, ConfigArgDefinition> argSpec;

    public MinimumOrderAmountCondition() {
        super(
                "minimum_order_amount",
                "If order total is greater than { amount }",
                10
        );
        Map<String, ConfigArgDefinition> argDefMap = new HashMap<>();
        ConfigArgDefinition argDef = new ConfigArgDefinition();
        argDef.setType("int");
        argDef.setUi(ImmutableMap.of("component", "currency-form-input"));
        argDefMap.put("amount", argDef);
        argSpec = argDefMap;
    }

    @Override
    public boolean check(OrderEntity orderEntity, ConfigArgValues argValues) {
        return orderEntity.getSubTotal() >= argValues.getInteger("amount");
    }

    @Override
    public Map<String, ConfigArgDefinition> getArgSpec() {
        return argSpec;
    }
}
