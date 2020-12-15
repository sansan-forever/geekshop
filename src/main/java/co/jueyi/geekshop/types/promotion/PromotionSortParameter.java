/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.promotion;

import co.jueyi.geekshop.types.common.SortOrder;
import lombok.Data;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Data
public class PromotionSortParameter {
    private SortOrder id;
    private SortOrder createdAt;
    private SortOrder updatedAt;
    private SortOrder startsAt;
    private SortOrder endsAt;
    private SortOrder couponCode;
    private SortOrder perCustomerUsageLimit;
    private SortOrder name;
}
