/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.config.payment_method;

import co.jueyi.geekshop.common.ConfigArgValues;
import co.jueyi.geekshop.common.ConfigurableOperationDef;
import co.jueyi.geekshop.entity.OrderEntity;
import co.jueyi.geekshop.entity.PaymentEntity;
import co.jueyi.geekshop.service.helpers.payment_state_machine.PaymentState;
import co.jueyi.geekshop.service.helpers.payment_state_machine.PaymentTransitionData;
import co.jueyi.geekshop.types.order.RefundOrderInput;
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
    public abstract CreateRefundResult createRefund(
            RefundOrderInput input,
            Integer total,
            OrderEntity orderEntity,
            PaymentEntity paymentEntity,
            ConfigArgValues argValues
    );

    /**
     * This function is called before the state of a Payment is transitioned. The return value is used to determine
     * whether the transition can occur.
     */
    public abstract  Object onStateTransitionStart(
            PaymentState fromState,
            PaymentState toState,
            PaymentTransitionData data);
}
