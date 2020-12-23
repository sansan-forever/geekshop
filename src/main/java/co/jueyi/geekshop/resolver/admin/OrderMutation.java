/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.custom.security.Allow;
import co.jueyi.geekshop.entity.FulfillmentEntity;
import co.jueyi.geekshop.entity.OrderEntity;
import co.jueyi.geekshop.entity.PaymentEntity;
import co.jueyi.geekshop.entity.RefundEntity;
import co.jueyi.geekshop.service.OrderService;
import co.jueyi.geekshop.service.helpers.ServiceHelper;
import co.jueyi.geekshop.service.helpers.order_state_machine.OrderState;
import co.jueyi.geekshop.types.common.DeletionResponse;
import co.jueyi.geekshop.types.common.Permission;
import co.jueyi.geekshop.types.history.HistoryEntry;
import co.jueyi.geekshop.types.order.*;
import co.jueyi.geekshop.types.payment.Payment;
import co.jueyi.geekshop.types.payment.Refund;
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
        return BeanMapper.map(paymentEntity, Payment.class);
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
        return BeanMapper.map(refundEntity, Refund.class);
    }

    @Allow(Permission.UpdateOrder)
    public Refund settleRefund(SettleRefundInput input, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        RefundEntity refundEntity = orderService.settleRefund(ctx, input);
        return BeanMapper.map(refundEntity, Refund.class);
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
