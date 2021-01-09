/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.eventbus.events;

import io.geekshop.common.RequestContext;
import io.geekshop.entity.OrderEntity;
import io.geekshop.entity.PaymentEntity;
import io.geekshop.service.helpers.payment_state_machine.PaymentState;
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
