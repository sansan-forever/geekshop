/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.payment;

import io.geekshop.types.common.SortOrder;
import lombok.Data;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Data
public class PaymentMethodSortParameter {
    private SortOrder id;
    private SortOrder createdAt;
    private SortOrder updatedAt;
    private SortOrder code;
}
