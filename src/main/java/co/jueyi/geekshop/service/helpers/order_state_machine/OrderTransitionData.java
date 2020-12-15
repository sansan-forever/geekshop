/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.service.helpers.order_state_machine;

import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.entity.OrderEntity;
import lombok.Data;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Data
public class OrderTransitionData {
    private RequestContext ctx;
    private OrderEntity orderEntity;
}
