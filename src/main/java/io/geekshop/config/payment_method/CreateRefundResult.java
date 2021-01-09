/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.config.payment_method;

import io.geekshop.service.helpers.refund_state_machine.RefundState;
import lombok.Data;

import java.util.Map;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Data
public class CreateRefundResult {
    private Integer amount;
    private RefundState state;
    private String transactionId;
    private Map<String, String> metadata;
}
