/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.payment;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Data
public class PaymentInput {
    private String method;
    private Map<String, String> metadata = new HashMap<>();
}
