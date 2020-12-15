/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.config.promotion;

import co.jueyi.geekshop.common.ConfigArgValues;
import co.jueyi.geekshop.common.ConfigurableOperationDef;
import co.jueyi.geekshop.entity.OrderEntity;
import co.jueyi.geekshop.entity.PromotionEntity;
import lombok.Getter;

/**
 * PromotionConditions are used to create {@link PromotionEntity}s. The purpose of a PromotionCondition
 * is to check the order against a particular predicate function (the `check` function) and to return `true` if the
 * Order satisfies the condition, or `false` if it does not.
 *
 * Created on Dec, 2020 by @author bobo
 */
@Getter
public abstract class PromotionCondition extends ConfigurableOperationDef {
    private final String code;
    private final String description;

    /**
     * Used to determine the order of application of multiple Promotions
     * on the same Order. See the {@link PromotionEntity} `priorityScore` field for
     * more information.
     */
    private final Integer priorityValue;

    protected PromotionCondition(
            String code,
            String description) {
        this(code, description,null);
    }

    protected PromotionCondition(
            String code,
            String description,
            Integer priorityValue) {
        this.code = code;
        this.description = description;
        this.priorityValue = priorityValue == null ? 0 : priorityValue;
    }

    public abstract boolean check(OrderEntity order, ConfigArgValues argValues);
}
