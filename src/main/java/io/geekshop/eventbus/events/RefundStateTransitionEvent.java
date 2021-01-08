/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.eventbus.events;

import io.geekshop.common.RequestContext;
import io.geekshop.entity.OrderEntity;
import io.geekshop.entity.RefundEntity;
import io.geekshop.service.helpers.refund_state_machine.RefundState;
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
