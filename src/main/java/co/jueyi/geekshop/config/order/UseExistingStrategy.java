/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.config.order;

import co.jueyi.geekshop.entity.OrderEntity;
import co.jueyi.geekshop.entity.OrderLineEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * The guest order is discarded and the existing order is used as the active order.
 *
 * Created on Dec, 2020 by @author bobo
 */
public class UseExistingStrategy implements OrderMergeStrategy {
    @Override
    public List<OrderLineEntity> merge(OrderEntity guestOrderEntity, OrderEntity existingOrderEntity) {
        return new ArrayList<>(existingOrderEntity.getLines());
    }
}
