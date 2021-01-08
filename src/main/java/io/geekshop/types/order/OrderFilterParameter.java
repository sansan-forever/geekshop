/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.types.order;

import io.geekshop.types.common.BooleanOperators;
import io.geekshop.types.common.DateOperators;
import io.geekshop.types.common.NumberOperators;
import io.geekshop.types.common.StringOperators;
import lombok.Data;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Data
public class OrderFilterParameter {
    private DateOperators createdAt;
    private DateOperators updatedAt;
    private StringOperators code;
    private StringOperators state;
    private BooleanOperators active;
    private NumberOperators subTotal;
    private NumberOperators total;
    private NumberOperators shipping;
}
