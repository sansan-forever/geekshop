/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.shipping;

import io.geekshop.types.common.ConfigurableOperationInput;
import lombok.Data;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Data
public class CreateShippingMethodInput {
    private String code;
    private String description;
    private ConfigurableOperationInput checker;
    private ConfigurableOperationInput calculator;
}
