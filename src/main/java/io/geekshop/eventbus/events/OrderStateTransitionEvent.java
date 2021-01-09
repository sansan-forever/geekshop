/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.eventbus.events;

import io.geekshop.common.RequestContext;
import io.geekshop.entity.OrderEntity;
import io.geekshop.service.helpers.order_state_machine.OrderState;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * This event is fired whenever an {@link io.geekshop.entity.OrderEntity} transitions from one
 * {@link io.geekshop.service.helpers.order_state_machine.OrderState} to another.
 *
 * Created on Dec, 2020 by @author bobo
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderStateTransitionEvent extends BaseEvent {
    private final OrderState fromState;
    private final OrderState toState;
    private final RequestContext ctx;
    private final OrderEntity order;
}
