/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.config.promotion;

import io.geekshop.common.ConfigurableOperationDef;
import lombok.Getter;


/**
 * An abstract class which is extended by {@link PromotionItemAction} and {@link PromotionOrderAction}.
 *
 * Created on Dec, 2020 by @author bobo
 */
@Getter
public abstract class PromotionAction extends ConfigurableOperationDef {
    private final String code;
    private final String description;
    /**
     * Used to determine the order of application of multiple Promotions
     * on the same Order. See the {@link Promotion} `priorityScore` field for more information.
     */
    private final Integer priorityValue;

    protected PromotionAction(
            String code, String description, Integer priorityValue) {
        this.code = code;
        this.description = description;
        this.priorityValue = priorityValue == null ? 0 : priorityValue;
    }
}
