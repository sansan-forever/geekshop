/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.common;

import io.geekshop.config.promotion.PromotionAction;
import io.geekshop.config.promotion.PromotionCondition;
import io.geekshop.config.shipping_method.ShippingCalculator;
import io.geekshop.config.shipping_method.ShippingEligibilityChecker;
import io.geekshop.types.common.ConfigArgDefinition;
import io.geekshop.types.common.ConfigurableOperationDefinition;
import org.apache.commons.lang3.BooleanUtils;

import java.util.Map;

/**
 * A ConfigurableOperationDef is a special type of object used extensively by GeekShop to define
 * code blocks which have arguments which are configurable at run-time by the administrator.
 *
 * This is the mechanism use by:
 *
 * * {@link io.geekshop.config.collection.CollectionFilter}
 * * {@link PaymentMethodHandler}
 * * {@link PromotionAction}
 * * {@link PromotionCondition}
 * * {@link ShippingCalculator}
 * * {@link ShippingEligibilityChecker}
 *
 *
 * Created on Nov, 2020 by @author bobo
 */
public abstract class ConfigurableOperationDef {
    public abstract String getCode();
    public abstract Map<String, ConfigArgDefinition> getArgSpec();
    public abstract String getDescription();


    /**
     * Convert a ConfigurableOperationDef into a ConfigurableOperationDefinition object, typically
     * so that it can be sent via the API.
     */
    public ConfigurableOperationDefinition toGraphQLType() {
        ConfigurableOperationDefinition configurableOperationDefinition = new ConfigurableOperationDefinition();
        configurableOperationDefinition.setCode(getCode());
        configurableOperationDefinition.setDescription(getDescription());
        getArgSpec().forEach((name, arg) -> {
            ConfigArgDefinition configArgDefinition = new ConfigArgDefinition();
            configArgDefinition.setName(name);
            configArgDefinition.setType(arg.getType());
            configArgDefinition.setList(BooleanUtils.toBoolean(arg.getList()));
            configArgDefinition.setUi(arg.getUi());
            configArgDefinition.setLabel(arg.getLabel());
            configArgDefinition.setDescription(arg.getDescription());
            configurableOperationDefinition.getArgs().add(configArgDefinition);
        });
        return configurableOperationDefinition;
    }
}
