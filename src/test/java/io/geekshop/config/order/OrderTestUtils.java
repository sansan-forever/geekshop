/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.config.order;

import io.geekshop.entity.OrderEntity;
import io.geekshop.entity.OrderItemEntity;
import io.geekshop.entity.OrderLineEntity;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created on Dec, 2020 by @author bobo
 */
public abstract class OrderTestUtils {
    public static OrderEntity createOrderFromLines(List<SimpleLine> simpleLines) {
        List<OrderLineEntity> lines = simpleLines.stream()
                .map(simpleLine -> {
                    OrderLineEntity line = new OrderLineEntity();
                    line.setId(simpleLine.getLineId());
                    line.setProductVariantId(simpleLine.getProductVariantId());
                    line.setItems(
                            IntStream.range(0, simpleLine.getQuantity())
                                    .mapToObj(i -> new OrderItemEntity()).collect(Collectors.toList())
                    );
                    return line;
                }).collect(Collectors.toList());
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setLines(lines);
        return orderEntity;
    }

    public static List<SimpleLine> parseLines(List<OrderLineEntity> lines) {
        return lines.stream().map(line -> {
            SimpleLine simpleLine = SimpleLine.builder()
                    .lineId(line.getId()).quantity(line.getQuantity()).productVariantId(line.getProductVariantId())
                    .build();
            return simpleLine;
        }).collect(Collectors.toList());
    }
}
