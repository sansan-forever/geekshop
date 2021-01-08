/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.config.order;

import io.geekshop.entity.OrderEntity;
import io.geekshop.entity.OrderLineEntity;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * If the existing order is empty, then the guest order is used. Otherwise the existing order is used.
 *
 * Created on Dec, 2020 by @author bobo
 */
public class UseGuestIfExistingEmptyStrategy implements OrderMergeStrategy {
    @Override
    public List<OrderLineEntity> merge(OrderEntity guestOrderEntity, OrderEntity existingOrderEntity) {
        return CollectionUtils.isEmpty(existingOrderEntity.getLines()) ? new ArrayList<>(guestOrderEntity.getLines())
                : new ArrayList<>(existingOrderEntity.getLines());
    }
}
