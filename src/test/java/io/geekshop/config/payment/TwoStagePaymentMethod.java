/*
 * Copyright (c) 2020 极客之道(daogeek.io).
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
import io.geekshop.exception.IllegalOperationException;
import io.geekshop.service.helpers.payment_state_machine.PaymentState;
import io.geekshop.service.helpers.payment_state_machine.PaymentTransitionData;
import io.geekshop.types.common.ConfigArgDefinition;
import io.geekshop.types.order.RefundOrderInput;
import com.google.common.collect.ImmutableMap;
import org.springframework.util.CollectionUtils;

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
        if (!CollectionUtils.isEmpty(metadata)) {
            result.setMetadata(metadata);
        }
        return result;
    }

    @Override
    public SettlePaymentResult settlePayment(
            OrderEntity orderEntity, PaymentEntity paymentEntity, ConfigArgValues argValues) {
        SettlePaymentResult result = new SettlePaymentResult();
        result.setSuccess(true);
        result.getMetadata().put("moreData", "42");
        return result;
    }

    @Override
    public Map<String, ConfigArgDefinition> getArgSpec() {
        return ImmutableMap.of();
    }
}
