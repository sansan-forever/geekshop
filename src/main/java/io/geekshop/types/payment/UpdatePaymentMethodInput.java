/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.payment;

import io.geekshop.types.common.ConfigArgInput;
import lombok.Data;

import java.util.List;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Data
public class UpdatePaymentMethodInput {
    private Long id;
    private String code;
    private Boolean enabled;
    private List<ConfigArgInput> configArgs;
}
