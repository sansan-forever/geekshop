/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.resolver.admin;

import io.geekshop.common.RequestContext;
import io.geekshop.common.utils.BeanMapper;
import io.geekshop.custom.security.Allow;
import io.geekshop.entity.FulfillmentEntity;
import io.geekshop.entity.OrderEntity;
import io.geekshop.entity.PaymentEntity;
import io.geekshop.entity.RefundEntity;
import io.geekshop.service.OrderService;
import io.geekshop.service.helpers.ServiceHelper;
import io.geekshop.service.helpers.order_state_machine.OrderState;
import io.geekshop.types.common.DeletionResponse;
import io.geekshop.types.common.Permission;
import io.geekshop.types.history.HistoryEntry;
import io.geekshop.types.order.*;
import io.geekshop.types.payment.Payment;
import io.geekshop.types.payment.Refund;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class OrderMutation implements GraphQLMutationResolver {
    private final OrderService orderService;
    /**
     * Mutation
     */
    @Allow(Permission.UpdateOrder)
    public Payment settlePayment(Long id, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        PaymentEntity paymentEntity = orderService.settlePayment(ctx, id);
        Payment payment = BeanMapper.map(paymentEntity, Payment.class);
        if (paymentEntity.getState() != null) {
            payment.setState(paymentEntity.getState().name());
        }
        return payment;
    }

    @Allow(Permission.UpdateOrder)
    public Fulfillment fulfillOrder(FulfillOrderInput input, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        FulfillmentEntity fulfillmentEntity = orderService.createFulfillment(ctx, input);
        return BeanMapper.map(fulfillmentEntity, Fulfillment.class);
    }

    @Allow(Permission.UpdateOrder)
    public Order cancelOrder(CancelOrderInput input, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        OrderEntity orderEntity = orderService.cancelOrder(ctx, input);
        return ServiceHelper.mapOrderEntityToOrder(orderEntity);
    }

    @Allow(Permission.UpdateOrder)
    public Refund refundOrder(RefundOrderInput input, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        RefundEntity refundEntity = orderService.refundOrder(ctx, input);
        Refund refund = BeanMapper.map(refundEntity, Refund.class);
        if (refundEntity.getState() != null) {
            refund.setState(refundEntity.getState().name());
        }
        return refund;
    }

    @Allow(Permission.UpdateOrder)
    public Refund settleRefund(SettleRefundInput input, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        RefundEntity refundEntity = orderService.settleRefund(ctx, input);
        Refund refund = BeanMapper.map(refundEntity, Refund.class);
        if (refundEntity.getState() != null) {
            refund.setState(refundEntity.getState().name());
        }
        return refund;
    }

    @Allow(Permission.UpdateOrder)
    public Order addNoteToOrder(AddNoteToOrderInput input, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        OrderEntity orderEntity = orderService.addNoteToOrder(ctx, input);
        return ServiceHelper.mapOrderEntityToOrder(orderEntity);
    }

    @Allow(Permission.UpdateOrder)
    public HistoryEntry updateOrderNote(UpdateOrderNoteInput input, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        return orderService.updateOrderNote(ctx, input);
    }

    @Allow(Permission.UpdateOrder)
    public DeletionResponse deleteOrderNote(Long id, DataFetchingEnvironment dfe) {
        return orderService.deleteOrderNote(id);
    }

    @Allow(Permission.UpdateOrder)
    public Order transitionOrderToStateByAdmin(Long id, String state, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        OrderEntity orderEntity = orderService.transitionToState(ctx, id, OrderState.valueOf(state));
        return ServiceHelper.mapOrderEntityToOrder(orderEntity);
    }
}
