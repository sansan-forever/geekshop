/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.types.common.DeletionResponse;
import co.jueyi.geekshop.types.history.HistoryEntry;
import co.jueyi.geekshop.types.order.*;
import co.jueyi.geekshop.types.payment.Payment;
import co.jueyi.geekshop.types.payment.Refund;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
public class OrderMutation implements GraphQLMutationResolver {
    /**
     * Mutation
     */
    public Payment settlePayment(Long id) {
        return null; // TODO
    }

    public Fulfillment fulfillOrder(FulfillOrderInput input) {
        return null; // TODO
    }

    public Order cancelOrder(CancelOrderInput input) {
        return null; // TODO
    }

    public Refund refundOrder(RefundOrderInput input) {
        return null; // TODO
    }

    public Refund settleRefund(SettleRefundInput input) {
        return null; // TODO
    }

    public Order addNoteToOrder(AddNoteToOrderInput input) {
        return null; // TODO
    }

    public HistoryEntry updateOrderNote(UpdateOrderNoteInput input) {
        return null; // TODO
    }

    public DeletionResponse deleteOrderNote(Long id) {
        return null; // TODO
    }

    public Order transitionOrderToState(Long id, String state) {
        return null; // TODO
    }

    public Order setOrderCustomerFields(UpdateOrderInput input) {
        return null; // TODO
    }
}
