/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.shipping;

import co.jueyi.geekshop.types.common.DateOperators;
import co.jueyi.geekshop.types.common.StringOperators;
import lombok.Data;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Data
public class ShippingMethodFilterParameter {
    private DateOperators createdAt;
    private DateOperators updatedAt;
    private StringOperators code;
    private StringOperators description;
}
