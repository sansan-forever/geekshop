/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.config.order;

import io.geekshop.entity.OrderEntity;
import io.geekshop.entity.OrderLineEntity;

import java.util.List;

/**
 * An OrderMergeStrategy defines what happens when a Customer with an existing Order
 * signs in with a guest Order, where both Orders may contain differing OrderLines.
 *
 * Somehow these differing OrderLines need to be reconciled into a single collection
 * of OrderLines. The OrderMergeStrategy defines the rules governing this reconciliation.
 *
 * Created on Dec, 2020 by @author bobo
 */
public interface OrderMergeStrategy {
    /**
     * Merges the lines of the guest Order with those of the existing Order which is associated
     * with the active customer.
     */
    List<OrderLineEntity> merge(OrderEntity guestOrderEntity, OrderEntity existingOrderEntity);
}
