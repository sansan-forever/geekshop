/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.service.helpers.refund_state_machine;

import io.geekshop.common.RequestContext;
import io.geekshop.entity.OrderEntity;
import io.geekshop.entity.RefundEntity;
import lombok.Data;

/**
 * The data which is passed to the state transition handlers of the RefundStateMachine.
 *
 * Created on Dec, 2020 by @author bobo
 */
@Data
public class RefundTransitionData {
    private RequestContext ctx;
    private OrderEntity orderEntity;
    private RefundEntity refundEntity;
}
