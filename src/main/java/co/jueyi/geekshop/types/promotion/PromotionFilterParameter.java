/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.promotion;

import co.jueyi.geekshop.types.common.BooleanOperators;
import co.jueyi.geekshop.types.common.DateOperators;
import co.jueyi.geekshop.types.common.NumberOperators;
import co.jueyi.geekshop.types.common.StringOperators;
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
