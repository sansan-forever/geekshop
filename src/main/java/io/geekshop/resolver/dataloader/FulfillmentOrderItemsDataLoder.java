/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.resolver.dataloader;

import io.geekshop.common.utils.BeanMapper;
import io.geekshop.entity.OrderItemEntity;
import io.geekshop.mapper.OrderItemEntityMapper;
import io.geekshop.service.helpers.ServiceHelper;
import io.geekshop.types.order.OrderItem;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.dataloader.MappedBatchLoader;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * Created on Dec, 2020 by @author bobo
 */
@SuppressWarnings("Duplicates")
public class FulfillmentOrderItemsDataLoder implements MappedBatchLoader<Long, List<OrderItem>> {
    private final OrderItemEntityMapper orderItemEntityMapper;

    public FulfillmentOrderItemsDataLoder(OrderItemEntityMapper orderItemEntityMapper) {
        this.orderItemEntityMapper = orderItemEntityMapper;
    }

    @Override
    public CompletionStage<Map<Long, List<OrderItem>>> load(Set<Long> fulfillmentIds) {
        return CompletableFuture.supplyAsync(() -> {
            QueryWrapper<OrderItemEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(OrderItemEntity::getFulfillmentId, fulfillmentIds);
            List<OrderItemEntity> orderItemEntities = this.orderItemEntityMapper.selectList(queryWrapper);

            if (orderItemEntities.size() == 0) {
                Map<Long, List<OrderItem>> emptyMap = new HashMap<>();
                fulfillmentIds.forEach(id -> emptyMap.put(id, new ArrayList<>()));
                return emptyMap;
            }

            Map<Long, List<OrderItem>> groupByFulfillmentId = orderItemEntities.stream()
                    .collect(Collectors.groupingBy(OrderItemEntity::getFulfillmentId,
                            Collectors.mapping(orderItemEntity ->
                                            ServiceHelper.mapOrderItemEntityToOrderItem(orderItemEntity),
                                    Collectors.toList()
                            )));

            return groupByFulfillmentId;
        });
    }
}
