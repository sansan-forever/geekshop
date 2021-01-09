/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.promotion;

import io.geekshop.types.common.BooleanOperators;
import io.geekshop.types.common.DateOperators;
import io.geekshop.types.common.NumberOperators;
import io.geekshop.types.common.StringOperators;
import lombok.Data;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Data
public class PromotionFilterParameter {
    private DateOperators createdAt;
    private DateOperators updatedAt;
    private DateOperators startsAt;
    private DateOperators endsAt;
    private StringOperators couponCode;
    private NumberOperators perCustomerUsageLimit;
    private StringOperators name;
    private BooleanOperators enabled;
}
