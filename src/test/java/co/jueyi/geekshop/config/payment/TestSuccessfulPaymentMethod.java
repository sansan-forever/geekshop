/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.config.payment;

import co.jueyi.geekshop.common.ConfigArgValues;
import co.jueyi.geekshop.config.payment_method.CreatePaymentResult;
import co.jueyi.geekshop.config.payment_method.PaymentMethodHandler;
import co.jueyi.geekshop.config.payment_method.SettlePaymentResult;
import co.jueyi.geekshop.entity.OrderEntity;
import co.jueyi.geekshop.entity.PaymentEntity;
import co.jueyi.geekshop.service.helpers.payment_state_machine.PaymentState;
import co.jueyi.geekshop.types.common.ConfigArgDefinition;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Created on Dec, 2020 by @author bobo
 */
public class TestSuccessfulPaymentMethod extends PaymentMethodHandler {

    public TestSuccessfulPaymentMethod() {
        super("test-payment-method", "Test Payment Method");
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
    public Map<String, ConfigArgDefinition> getArgSpec() {
        return ImmutableMap.of();
    }
}
