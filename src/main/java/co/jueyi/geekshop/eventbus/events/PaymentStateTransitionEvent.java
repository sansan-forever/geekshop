/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.eventbus.events;

import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.entity.OrderEntity;
import co.jueyi.geekshop.entity.PaymentEntity;
import co.jueyi.geekshop.service.helpers.payment_state_machine.PaymentState;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * This event is fired whenever a {@link PaymentEntity} transitions from one {@link PaymentState} to another, e.g.
 * a Payment is authorized by the payment provider.
 *
 * Created on Dec, 2020 by @author bobo
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PaymentStateTransitionEvent extends BaseEvent {
    private final PaymentState fromState;
    private final PaymentState toState;
    private final RequestContext ctx;
    private final PaymentEntity payment;
    private final OrderEntity order;
}
