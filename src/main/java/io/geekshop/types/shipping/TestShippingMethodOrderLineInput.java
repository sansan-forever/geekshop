/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.shipping;

import lombok.Data;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Data
public class TestShippingMethodOrderLineInput {
    private Long productVariantId;
    private Integer quantity;
}
