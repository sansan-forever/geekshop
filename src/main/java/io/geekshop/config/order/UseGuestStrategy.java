/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.config.order;

import io.geekshop.entity.OrderEntity;
import io.geekshop.entity.OrderLineEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Any existing order is discarded and the guest order is set as the active order.
 *
 * Created on Dec, 2020 by @author bobo
 */
public class UseGuestStrategy implements OrderMergeStrategy {
    @Override
    public List<OrderLineEntity> merge(OrderEntity guestOrderEntity, OrderEntity existingOrderEntity) {
        return new ArrayList<>(guestOrderEntity.getLines());
    }
}
