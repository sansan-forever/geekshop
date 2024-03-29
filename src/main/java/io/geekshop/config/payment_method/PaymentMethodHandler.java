/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.config.payment_method;

import io.geekshop.common.ConfigArgValues;
import io.geekshop.common.ConfigurableOperationDef;
import io.geekshop.entity.OrderEntity;
import io.geekshop.entity.PaymentEntity;
import io.geekshop.service.helpers.payment_state_machine.PaymentState;
import io.geekshop.service.helpers.payment_state_machine.PaymentTransitionData;
import io.geekshop.types.order.RefundOrderInput;
import lombok.Getter;

import java.util.Map;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Getter
public abstract class PaymentMethodHandler extends ConfigurableOperationDef {

    private final String code;
    private final String description;

    protected PaymentMethodHandler(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * To create a new payment
     */
    public abstract CreatePaymentResult createPayment(
            OrderEntity orderEntity,
            ConfigArgValues argValues,
            Map<String, String> metadata
    );


    /**
     * To settle a payment
     */
    public abstract SettlePaymentResult settlePayment(
            OrderEntity orderEntity,
            PaymentEntity paymentEntity,
            ConfigArgValues argValues
    );

    /**
     * To create a refund
     */
    public CreateRefundResult createRefund(
            RefundOrderInput input,
            Integer total,
            OrderEntity orderEntity,
            PaymentEntity paymentEntity,
            ConfigArgValues argValues) {
        return null; // 默认实现返回null
    }

    /**
     * This function is called before the state of a Payment is transitioned. The return value is used to determine
     * whether the transition can occur.
     */
    public Object onStateTransitionStart(
            PaymentState fromState,
            PaymentState toState,
            PaymentTransitionData data) {
        return true; // 默认实现返回true
    }
}
