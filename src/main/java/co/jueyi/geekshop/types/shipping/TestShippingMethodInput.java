/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.shipping;

import co.jueyi.geekshop.types.common.ConfigurableOperationInput;
import co.jueyi.geekshop.types.common.CreateAddressInput;
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
