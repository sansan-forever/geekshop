/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.service.helpers.payment_state_machine;

import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.entity.OrderEntity;
import co.jueyi.geekshop.entity.PaymentEntity;
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
