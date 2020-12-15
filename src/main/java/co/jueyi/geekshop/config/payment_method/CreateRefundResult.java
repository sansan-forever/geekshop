/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.config.payment_method;

import co.jueyi.geekshop.service.helpers.refund_state_machine.RefundState;
import lombok.Data;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Data
public class CreateRefundResult {
    private RefundState state;
    private String transactionId;
    private Object metadata;
}
