/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.resolver.dataloader;

import io.geekshop.common.utils.BeanMapper;
import io.geekshop.entity.OrderItemEntity;
import io.geekshop.mapper.OrderItemEntityMapper;
import io.geekshop.service.helpers.ServiceHelper;
import io.geekshop.types.order.OrderItem;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.dataloader.MappedBatchLoader;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * Created on Dec, 2020 by @author bobo
 */
@SuppressWarnings("Duplicates")
@RequiredArgsConstructor
public class RefundOrderItemsDataLoader implements MappedBatchLoader<Long, List<OrderItem>> {

    private final OrderItemEntityMapper orderItemEntityMapper;

    @Override
    public CompletionStage<Map<Long, List<OrderItem>>> load(Set<Long> refundIds) {
        return CompletableFuture.supplyAsync(() -> {
            QueryWrapper<OrderItemEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(OrderItemEntity::getRefundId, refundIds);
            List<OrderItemEntity> orderItemEntities = this.orderItemEntityMapper.selectList(queryWrapper);

            if (orderItemEntities.size() == 0) {
                Map<Long, List<OrderItem>> emptyMap = new HashMap<>();
                refundIds.forEach(id -> emptyMap.put(id, new ArrayList<>()));
                return emptyMap;
            }

            Map<Long, List<OrderItem>> groupByRefundId = orderItemEntities.stream()
                    .collect(Collectors.groupingBy(OrderItemEntity::getRefundId,
                            Collectors.mapping(orderItemEntity ->
                                            ServiceHelper.mapOrderItemEntityToOrderItem(orderItemEntity),
                                    Collectors.toList()
                            )));

            return groupByRefundId;
        });
    }
}
