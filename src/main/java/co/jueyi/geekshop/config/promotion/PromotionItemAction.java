/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.config.promotion;

import co.jueyi.geekshop.common.ConfigArgValues;
import co.jueyi.geekshop.entity.OrderItemEntity;
import co.jueyi.geekshop.entity.OrderLineEntity;

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
