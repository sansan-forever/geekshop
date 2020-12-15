/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.config.payment_method;

import co.jueyi.geekshop.service.helpers.payment_state_machine.PaymentState;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Data
public class CreatePaymentResult {
    private Integer amount;
    private PaymentState state;
    private String transactionId;
    private String errorMessage;
    private Map<String, String> metadata = new HashMap<>();
}
