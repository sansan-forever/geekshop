/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.order;

import co.jueyi.geekshop.types.common.SortOrder;
import lombok.Data;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Data
public class OrderSortParameter {
    private SortOrder id;
    private SortOrder createdAt;
    private SortOrder updatedAt;
    private SortOrder code;
    private SortOrder state;
    private SortOrder subTotal;
    private SortOrder shipping;
    private SortOrder total;
}
