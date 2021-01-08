/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.service.helpers.order_state_machine;

import io.geekshop.common.RequestContext;
import io.geekshop.entity.OrderEntity;
import lombok.Data;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Data
public class OrderTransitionData {
    private RequestContext ctx;
    private OrderEntity orderEntity;
}
