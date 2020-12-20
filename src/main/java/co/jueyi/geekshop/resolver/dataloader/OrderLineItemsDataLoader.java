/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.dataloader;

import co.jueyi.geekshop.entity.OrderItemEntity;
import co.jueyi.geekshop.mapper.OrderItemEntityMapper;
import co.jueyi.geekshop.service.helpers.ServiceHelper;
import co.jueyi.geekshop.types.order.OrderItem;
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
public class OrderLineItemsDataLoader implements MappedBatchLoader<Long, List<OrderItem>> {

    private final OrderItemEntityMapper orderItemEntityMapper;

    @Override
    public CompletionStage<Map<Long, List<OrderItem>>> load(Set<Long> orderLineIds) {
        return CompletableFuture.supplyAsync(() -> {
            QueryWrapper<OrderItemEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(OrderItemEntity::getOrderLineId, orderLineIds);
            List<OrderItemEntity> orderItemEntities = this.orderItemEntityMapper.selectList(queryWrapper);

            if (orderItemEntities.size() == 0) {
                Map<Long, List<OrderItem>> emptyMap = new HashMap<>();
                orderLineIds.forEach(id -> emptyMap.put(id, new ArrayList<>()));
                return emptyMap;
            }

            Map<Long, List<OrderItem>> groupByOrderLineId = orderItemEntities.stream()
                    .collect(Collectors.groupingBy(OrderItemEntity::getOrderLineId,
                            Collectors.mapping(orderItemEntity ->
                                            ServiceHelper.mapOrderItemEntityToOrderItem(orderItemEntity),
                                    Collectors.toList()
                            )));

            return groupByOrderLineId;
        });
    }
}
