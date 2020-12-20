/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.service.helpers;

import co.jueyi.geekshop.entity.OrderEntity;
import co.jueyi.geekshop.entity.OrderItemEntity;
import co.jueyi.geekshop.entity.OrderLineEntity;
import co.jueyi.geekshop.entity.PaymentEntity;
import co.jueyi.geekshop.mapper.PaymentEntityMapper;
import co.jueyi.geekshop.service.helpers.payment_state_machine.PaymentState;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class OrderHelper {
    private final PaymentEntityMapper paymentEntityMapper;

    /**
     * Returns true if the Order total is covered by Payments in the specified state.
     */
    public boolean orderTotalIsCovered(OrderEntity orderEntity, PaymentState state) {
        QueryWrapper<PaymentEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PaymentEntity::getOrderId, orderEntity.getId());
        List<PaymentEntity> paymentEntities = this.paymentEntityMapper.selectList(queryWrapper);
        List<PaymentEntity> filteredPaymentEntities = paymentEntities.stream()
                .filter(p -> Objects.equals(p.getState(), state)).collect(Collectors.toList());
        int sum = 0;
        for(PaymentEntity paymentEntity: filteredPaymentEntities) {
            sum += paymentEntity.getAmount();
        }
        return sum >= orderEntity.getTotal();
    }

    public boolean orderItemsAreFulfilled(OrderEntity orderEntity) {
        return getOrderItems(orderEntity).stream()
                .filter(orderItemEntity -> !orderItemEntity.isCancelled())
                .allMatch(this::isFulfilled);
    }

    /**
     * Returns true if at least one, but not all (non-cancelled) OrderItems are fulfilled.
     */
    public boolean orderItemArePartiallyFulfilled(OrderEntity orderEntity) {
        List<OrderItemEntity> nonCancelledItems = getOrderItems(orderEntity).stream()
                .filter(orderItemEntity -> !orderItemEntity.isCancelled()).collect(Collectors.toList());
        return nonCancelledItems.stream().anyMatch(this::isFulfilled) &&
                !nonCancelledItems.stream().allMatch(this::isFulfilled);
    }

    public List<Long> getOrderItemIds(OrderEntity order) {
        return getOrderItems(order).stream().map(OrderItemEntity::getId).collect(Collectors.toList());
    }

    public List<Long> getOrderItemIds(OrderLineEntity line) {
        return line.getItems().stream().map(OrderItemEntity::getId).collect(Collectors.toList());
    }

    public List<Long> getOrderLineIds(OrderEntity order) {
        return order.getLines().stream().map(OrderLineEntity::getId).collect(Collectors.toList());
    }

    /**
     * Returns true if all OrderItems in the order are cancelled.
     */
    public boolean orderItemsAreAllCancelled(OrderEntity orderEntity) {
        return getOrderItems(orderEntity).stream().allMatch(item -> item.isCancelled());
    }

    private List<OrderItemEntity> getOrderItems(OrderEntity orderEntity) {
        List<OrderItemEntity> result = new ArrayList<>();
        orderEntity.getLines().forEach(line -> {
            result.addAll(line.getItems());
        });
        return result;
    }

    private boolean isFulfilled(OrderItemEntity orderItemEntity) {
        return orderItemEntity.getFulfillmentId() != null;
    }
}
