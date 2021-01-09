/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.config.promotion.conditions;

import io.geekshop.common.ConfigArgValues;
import io.geekshop.config.promotion.PromotionCondition;
import io.geekshop.entity.OrderEntity;
import io.geekshop.entity.OrderLineEntity;
import io.geekshop.types.common.ConfigArgDefinition;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on Dec, 2020 by @author bobo
 */
@SuppressWarnings("Duplicates")
public class ContainsProductsCondition extends PromotionCondition {

    private final Map<String, ConfigArgDefinition> argSpec;

    public ContainsProductsCondition() {
        super(
                "contains_products",
                "Buy at least { minimum } of the specified products"
        );

        Map<String, ConfigArgDefinition> argDefMap = new HashMap<>();
        ConfigArgDefinition argDef = new ConfigArgDefinition();
        argDef.setType("int");
        argDefMap.put("minimum", argDef);

        argDef = new ConfigArgDefinition();
        argDef.setType("ID");
        argDef.setList(true);
        argDef.setUi(ImmutableMap.of("component", "product-selector-form-input"));
        argDef.setLabel("Product variants");
        argDefMap.put("productVariantIds", argDef);

        argSpec = argDefMap;
    }

    @Override
    public boolean check(OrderEntity orderEntity, ConfigArgValues argValues) {
        List<Long> ids = argValues.getIdList("productVariantIds");
        int matches = 0;
        for(OrderLineEntity line : orderEntity.getLines()) {
            if (ids.contains(line.getProductVariantId())) {
                matches += line.getQuantity();
            }
        }
        return matches >= argValues.getInteger("minimum");
    }

    @Override
    public Map<String, ConfigArgDefinition> getArgSpec() {
        return argSpec;
    }
}
