/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.config.promotion;

import io.geekshop.common.ConfigArgValues;
import io.geekshop.entity.OrderEntity;

/**
 * Represents a PromotionAction which applies to the {@link OrderEntity} as a whole.
 *
 * Created on Dec, 2020 by @author bobo
 */
public abstract class PromotionOrderAction extends PromotionAction {
    protected PromotionOrderAction(
            String code,
            String description) {
        super(code, description, null);
    }

    protected PromotionOrderAction(
            String code,
            String description,
            Integer priorityValue) {
        super(code, description, priorityValue);
    }

    public abstract float execute(OrderEntity order, ConfigArgValues argValues);
}
