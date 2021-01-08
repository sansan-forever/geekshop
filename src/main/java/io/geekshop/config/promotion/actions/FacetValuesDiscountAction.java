/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.config.promotion.actions;

import io.geekshop.common.ConfigArgValues;
import io.geekshop.config.promotion.PromotionItemAction;
import io.geekshop.config.promotion.utils.FacetValueChecker;
import io.geekshop.entity.OrderItemEntity;
import io.geekshop.entity.OrderLineEntity;
import io.geekshop.types.common.ConfigArgDefinition;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on Dec, 2020 by @author bobo
 */
public class FacetValuesDiscountAction extends PromotionItemAction {
    @Autowired
    private FacetValueChecker facetValueChecker;

    private final  Map<String, ConfigArgDefinition> argSpec;

    public FacetValuesDiscountAction() {
        super(
                "facet_based_discount",
                "Discount products with these facets by { discount }%"
        );
        Map<String, ConfigArgDefinition> argDefMap = new HashMap<>();
        ConfigArgDefinition argDef = new ConfigArgDefinition();

        argDef.setType("int");
        argDef.setUi(ImmutableMap.of("component", "number-form-input", "suffix", "%"));
        argDefMap.put("discount", argDef);

        argDef = new ConfigArgDefinition();
        argDef.setType("ID");
        argDef.setList(true);
        argDef.setUi(ImmutableMap.of("component", "facet-value-form-input"));
        argDefMap.put("facets", argDef);

        argSpec = argDefMap;
    }

    @Override
    public float execute(OrderItemEntity orderItemEntity, OrderLineEntity orderLineEntity, ConfigArgValues argValues) {
        if (facetValueChecker.hasFacetValues(orderLineEntity, argValues.getIdList("facets"))) {
            return orderItemEntity.getUnitPriceWithPromotions() *
                    (argValues.getInteger("discount") / 100.0F) * -1;
        }
        return 0.0F;
    }

    @Override
    public Map<String, ConfigArgDefinition> getArgSpec() {
        return argSpec;
    }
}
