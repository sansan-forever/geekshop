/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.config.payment;

import io.geekshop.common.ConfigArgValues;
import io.geekshop.config.payment_method.CreatePaymentResult;
import io.geekshop.config.payment_method.CreateRefundResult;
import io.geekshop.config.payment_method.PaymentMethodHandler;
import io.geekshop.config.payment_method.SettlePaymentResult;
import io.geekshop.entity.OrderEntity;
import io.geekshop.entity.PaymentEntity;
import io.geekshop.service.helpers.payment_state_machine.PaymentState;
import io.geekshop.service.helpers.payment_state_machine.PaymentTransitionData;
import io.geekshop.service.helpers.refund_state_machine.RefundState;
import io.geekshop.types.common.ConfigArgDefinition;
import io.geekshop.types.order.RefundOrderInput;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * A payment method which includes a createRefund method.
 *
 * Created on Dec, 2020 by @author bobo
 */
public class SingleStageRefundablePaymentMethod extends PaymentMethodHandler {
    public SingleStageRefundablePaymentMethod() {
        super("single-stage-refundable-payment-method", "Test Payment Method");
    }

    @Override
    public CreatePaymentResult createPayment(
            OrderEntity orderEntity, ConfigArgValues argValues, Map<String, String> metadata) {
        CreatePaymentResult result = new CreatePaymentResult();
        result.setAmount(orderEntity.getTotal());
        result.setState(PaymentState.Settled);
        result.setTransactionId("12345");
        result.setMetadata(metadata);
        return result;
    }

    @Override
    public SettlePaymentResult settlePayment(
            OrderEntity orderEntity, PaymentEntity paymentEntity, ConfigArgValues argValues) {
        SettlePaymentResult result = new SettlePaymentResult();
        result.setSuccess(true);
        return result;
    }

    @Override
    public CreateRefundResult createRefund(
            RefundOrderInput input,
            Integer total,
            OrderEntity orderEntity,
            PaymentEntity paymentEntity,
            ConfigArgValues argValues) {
        CreateRefundResult result = new CreateRefundResult();
        result.setAmount(total);
        result.setState(RefundState.Settled);
        result.setTransactionId("abc123");
        return result;
    }

    @Override
    public Map<String, ConfigArgDefinition> getArgSpec() {
        return ImmutableMap.of();
    }
}
