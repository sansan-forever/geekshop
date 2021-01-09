/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.payment;

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
