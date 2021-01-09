/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.service.helpers.payment_state_machine;

import io.geekshop.common.RequestContext;
import io.geekshop.entity.OrderEntity;
import io.geekshop.entity.PaymentEntity;
import lombok.Data;

/**
 * The data which is passed to the `onStateTransitionStart` function configured when constructing
 * a new `PaymentMethodHandler`
 *
 * Created on Dec, 2020 by @author bobo
 */
@Data
public class PaymentTransitionData {
    private RequestContext ctx;
    private PaymentEntity paymentEntity;
    private OrderEntity orderEntity;
}
