/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.shipping;

import co.jueyi.geekshop.types.common.ConfigurableOperationInput;
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
