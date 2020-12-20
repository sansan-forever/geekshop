/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.dataloader;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.entity.OrderEntity;
import co.jueyi.geekshop.mapper.OrderEntityMapper;
import co.jueyi.geekshop.types.order.Order;
import org.dataloader.MappedBatchLoader;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * Created on Dec, 2020 by @author bobo
 */
public class OrderDataLoader implements MappedBatchLoader<Long, Order> {
    private final OrderEntityMapper orderEntityMapper;

    public OrderDataLoader(OrderEntityMapper orderEntityMapper) {
        this.orderEntityMapper = orderEntityMapper;
    }

    @Override
    public CompletionStage<Map<Long, Order>> load(Set<Long> orderIds) {
        return CompletableFuture.supplyAsync(() -> {
            List<OrderEntity> orderEntities =
                    this.orderEntityMapper.selectBatchIds(orderIds);
            List<Order> orders = orderEntities.stream()
                    .map(orderEntity -> BeanMapper.map(orderEntity, Order.class))
                    .collect(Collectors.toList());
            Map<Long, Order> orderMap = orders.stream()
                    .collect(Collectors.toMap(Order::getId, order -> order));
            return orderMap;
        });
    }
}
