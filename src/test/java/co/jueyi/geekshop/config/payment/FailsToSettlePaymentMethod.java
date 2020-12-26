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
 * A payment method where calling `settlePayment` always fails.
 *
 * Created on Dec, 2020 by @author bobo
 */
public class FailsToSettlePaymentMethod extends PaymentMethodHandler {
    public FailsToSettlePaymentMethod() {
        super("fails-to-settle-payment-method", "Test Payment Method");
    }

    @Override
    public CreatePaymentResult createPayment(
            OrderEntity orderEntity, ConfigArgValues argValues, Map<String, String> metadata) {
        CreatePaymentResult result = new CreatePaymentResult();
        result.setAmount(orderEntity.getTotal());
        result.setState(PaymentState.Authorized);
        result.setTransactionId("12345");
        result.setMetadata(metadata);
        return result;
    }

    @Override
    public SettlePaymentResult settlePayment(
            OrderEntity orderEntity, PaymentEntity paymentEntity, ConfigArgValues argValues) {
        SettlePaymentResult result = new SettlePaymentResult();
        result.setSuccess(false);
        result.setErrorMessage("Something went horribly wrong");
        return result;
    }

    @Override
    public Map<String, ConfigArgDefinition> getArgSpec() {
        return ImmutableMap.of();
    }
}
