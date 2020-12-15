/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.service.helpers.refund_state_machine;

import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.entity.OrderEntity;
import co.jueyi.geekshop.entity.RefundEntity;
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
