/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.service.helpers.order_merger;

import co.jueyi.geekshop.config.order.OrderMergeOptions;
import co.jueyi.geekshop.entity.OrderEntity;
import co.jueyi.geekshop.entity.OrderLineEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class OrderMerger {
    private final OrderMergeOptions orderMergeOptions;

    /**
     * Applies the configured OrderMergeStrategy to the supplied guestOrder and existingOrder. Returns an object
     * containing entities which then need to be persisted to the database by the OrderService methods.
     */
    public MergeResult merge(OrderEntity guestOrder, OrderEntity existingOrder) {
        if (!this.orderEmpty(guestOrder) && !this.orderEmpty(existingOrder)) {
            List<OrderLineEntity> mergeLines = orderMergeOptions.getMergeStrategy().merge(guestOrder, existingOrder);
            return MergeResult.builder()
                    .orderEntity(existingOrder)
                    .linesToInsert(this.getLinesToInsert(existingOrder, mergeLines))
                    .orderToDelete(guestOrder)
                    .build();
        } else if (!this.orderEmpty(guestOrder) && this.orderEmpty(existingOrder)) {
            return MergeResult.builder()
                    .orderEntity(guestOrder)
                    .orderToDelete(existingOrder)
                    .build();
        } else if (this.orderEmpty(guestOrder) && !this.orderEmpty(existingOrder)) {
            return MergeResult.builder()
                    .orderEntity(existingOrder)
                    .orderToDelete(guestOrder)
                    .build();
        } else {
            return MergeResult.builder().build();
        }
    }

    private List<LineItem> getLinesToInsert(OrderEntity existingOrder, List<OrderLineEntity> mergedLines) {
        List<LineItem> linesToInsert = new ArrayList<>();
        for(OrderLineEntity line: mergedLines) {
            if (!existingOrder.getLines().stream().anyMatch(existingLine ->
                    Objects.equals(existingLine.getProductVariantId(), line.getProductVariantId()))) {
                linesToInsert.add(LineItem.builder()
                        .productVariantId(line.getProductVariantId()).quantity(line.getQuantity()).build());
            }
        }
        return linesToInsert;
    }

    private boolean orderEmpty(OrderEntity orderEntity) {
        return orderEntity == null || CollectionUtils.isEmpty(orderEntity.getLines());
    }
}
