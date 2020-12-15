/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.config.promotion;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Configures the Conditions and Actions available when creating Promotions.
 *
 * Created on Dec, 2020 by @author bobo
 */
@Getter
@RequiredArgsConstructor
public class PromotionOptions {
    /**
     * A list of conditions which can be used to construct Promotions
     */
    private final List<PromotionCondition> promotionConditions;
    /**
     * A list of actions which can be used to construct Promotions
     */
    private final List<PromotionAction> promotionActions;
}
