/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.eventbus.events;

import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.entity.OrderEntity;
import co.jueyi.geekshop.service.helpers.order_state_machine.OrderState;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * This event is fired whenever an {@link co.jueyi.geekshop.entity.OrderEntity} transitions from one
 * {@link co.jueyi.geekshop.service.helpers.order_state_machine.OrderState} to another.
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
