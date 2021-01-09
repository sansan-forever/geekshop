/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.shipping;

import io.geekshop.types.common.ConfigurableOperationInput;
import io.geekshop.types.common.CreateAddressInput;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Data
public class TestShippingMethodInput {
    private ConfigurableOperationInput checker;
    private ConfigurableOperationInput calculator;
    private CreateAddressInput shippingAddress;
    private List<TestShippingMethodOrderLineInput> lines = new ArrayList<>();
}
