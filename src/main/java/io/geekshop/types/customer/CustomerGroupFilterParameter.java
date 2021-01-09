/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.customer;

import io.geekshop.types.common.DateOperators;
import io.geekshop.types.common.StringOperators;
import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class CustomerGroupFilterParameter {
    private StringOperators name;
    private DateOperators createdAt;
    private DateOperators updatedAt;
}
