/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.order;

import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class OrderLineInput {
    private Long orderLineId;
    private Integer quantity;
}
