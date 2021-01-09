/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.config.promotion;

import io.geekshop.common.ConfigArgValues;
import io.geekshop.entity.OrderItemEntity;
import io.geekshop.entity.OrderLineEntity;

/**
 * Represents a PromotionAction which applies to individual {@link OrderItemEntity}
 *
 * Created on Dec, 2020 by @author bobo
 */
public abstract class PromotionItemAction extends PromotionAction {

    protected PromotionItemAction(
            String code,
            String description) {
        super(code, description, null);
    }
    protected PromotionItemAction(String code,
                               String description,
                               Integer priorityValue) {
        super(code, description, priorityValue);
    }

    public abstract float execute(OrderItemEntity orderItem,
                         OrderLineEntity orderLine,
                         ConfigArgValues argValues);
}
