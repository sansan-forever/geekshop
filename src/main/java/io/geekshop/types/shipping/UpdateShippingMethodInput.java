/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.types.shipping;

import io.geekshop.types.common.ConfigurableOperationInput;
import lombok.Data;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Data
public class UpdateShippingMethodInput {
    private Long id;
    private String code;
    private String description;
    private ConfigurableOperationInput checker;
    private ConfigurableOperationInput calculator;
}
