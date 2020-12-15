/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.config.promotion;

import co.jueyi.geekshop.common.ConfigArgValues;
import co.jueyi.geekshop.entity.OrderEntity;

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
