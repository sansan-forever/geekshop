/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.config.payment_method;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Data
public class SettlePaymentResult {
    private boolean success;
    private String errorMessage;
    private Map<String, String> metadata = new HashMap<>();
}
