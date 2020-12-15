/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.shipping;

import lombok.Data;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Data
public class TestShippingMethodOrderLineInput {
    private Long productVariantId;
    private Integer quantity;
}
