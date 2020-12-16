/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.eventbus.events;

import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.entity.OrderEntity;
import co.jueyi.geekshop.entity.RefundEntity;
import co.jueyi.geekshop.service.helpers.refund_state_machine.RefundState;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * This event is fired whenever a {@link RefundEntity} transitions from one {@link RefundState} to another.
 *
 * Created on Dec, 2020 by @author bobo
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RefundStateTransitionEvent extends BaseEvent {
    private final RefundState fromState;
    private final RefundState toState;
    private final RequestContext ctx;
    private final RefundEntity refund;
    private final OrderEntity order;
}
