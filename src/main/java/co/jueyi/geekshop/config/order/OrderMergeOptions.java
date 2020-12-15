/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.config.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Getter
@RequiredArgsConstructor
public class OrderMergeOptions {
    /**
     * Defines the strategy used to merge a guest Order and an existing Order when
     * signing in.
     */
    private final OrderMergeStrategy mergeStrategy;
    /**
     * Defines the strategy used to merge a guest Order and an existing Order when
     * signing in as part of the checkout flow.
     */
    private final OrderMergeStrategy checkoutMergeStrategy;
}
