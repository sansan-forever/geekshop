/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.common;

import co.jueyi.geekshop.config.promotion.PromotionAction;
import co.jueyi.geekshop.config.promotion.PromotionCondition;
import co.jueyi.geekshop.config.shipping_method.ShippingCalculator;
import co.jueyi.geekshop.config.shipping_method.ShippingEligibilityChecker;
import co.jueyi.geekshop.types.common.ConfigArgDefinition;
import co.jueyi.geekshop.types.common.ConfigurableOperationDefinition;
import org.apache.commons.lang3.BooleanUtils;

import java.util.Map;

/**
 * A ConfigurableOperationDef is a special type of object used extensively by GeekShop to define
 * code blocks which have arguments which are configurable at run-time by the administrator.
 *
 * This is the mechanism use by:
 *
 * * {@link co.jueyi.geekshop.config.collection.CollectionFilter}
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
