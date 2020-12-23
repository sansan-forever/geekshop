/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.config.payment;

import co.jueyi.geekshop.common.ConfigArgValues;
import co.jueyi.geekshop.config.payment_method.CreatePaymentResult;
import co.jueyi.geekshop.config.payment_method.CreateRefundResult;
import co.jueyi.geekshop.config.payment_method.PaymentMethodHandler;
import co.jueyi.geekshop.config.payment_method.SettlePaymentResult;
import co.jueyi.geekshop.entity.OrderEntity;
import co.jueyi.geekshop.entity.PaymentEntity;
import co.jueyi.geekshop.exception.IllegalOperationException;
import co.jueyi.geekshop.service.helpers.payment_state_machine.PaymentState;
import co.jueyi.geekshop.service.helpers.payment_state_machine.PaymentTransitionData;
import co.jueyi.geekshop.types.common.ConfigArgDefinition;
import co.jueyi.geekshop.types.order.RefundOrderInput;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * A two-stage (authorize, capture) payment method, with no createRefund method.
 *
 * Created on Dec, 2020 by @author bobo
 */
public class TwoStagePaymentMethod extends PaymentMethodHandler {
    public TwoStagePaymentMethod() {
        super("authorize-only-payment-method", "Test Payment Method");
    }

    @Override
    public CreatePaymentResult createPayment(
            OrderEntity orderEntity, ConfigArgValues argValues, Map<String, String> metadata) {
        CreatePaymentResult result = new CreatePaymentResult();
        result.setAmount(orderEntity.getTotal());
        result.setState(PaymentState.Authorized);
        result.setTransactionId("12345");
        return result;
    }

    @Override
    public SettlePaymentResult settlePayment(
            OrderEntity orderEntity, PaymentEntity paymentEntity, ConfigArgValues argValues) {
        SettlePaymentResult result = new SettlePaymentResult();
        result.setSuccess(true);
        result.getMetadata().put("moreData", "42");
        return null;
    }

    @Override
    public CreateRefundResult createRefund(
            RefundOrderInput input,
            Integer total,
            OrderEntity orderEntity,
            PaymentEntity paymentEntity,
            ConfigArgValues argValues) {
        throw new IllegalOperationException("Not supported operation");
    }

    @Override
    public Object onStateTransitionStart(PaymentState fromState, PaymentState toState, PaymentTransitionData data) {
        return null;
    }

    @Override
    public Map<String, ConfigArgDefinition> getArgSpec() {
        return ImmutableMap.of();
    }
}
