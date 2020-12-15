/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.config.promotion.actions;

import co.jueyi.geekshop.common.ConfigArgValues;
import co.jueyi.geekshop.config.promotion.PromotionOrderAction;
import co.jueyi.geekshop.entity.OrderEntity;
import co.jueyi.geekshop.types.common.ConfigArgDefinition;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on Dec, 2020 by @author bobo
 */
@SuppressWarnings("Duplicates")
public class OrderPercentageDiscount extends PromotionOrderAction {

    private final  Map<String, ConfigArgDefinition> argSpec;

    public OrderPercentageDiscount() {
        super(
                "order_percentage_discount",
                "Discount order by { discount }%"
        );
        Map<String, ConfigArgDefinition> argDefMap = new HashMap<>();
        ConfigArgDefinition argDef = new ConfigArgDefinition();

        argDef.setType("int");
        argDef.setUi(ImmutableMap.of("component", "number-form-input", "suffix", "%"));
        argDefMap.put("discount", argDef);

        argSpec = argDefMap;
    }

    @Override
    public float execute(OrderEntity orderEntity, ConfigArgValues argValues) {
        return orderEntity.getSubTotal() * (argValues.getInteger("discount") / 100.0F) * -1;
    }

    @Override
    public Map<String, ConfigArgDefinition> getArgSpec() {
        return argSpec;
    }
}
