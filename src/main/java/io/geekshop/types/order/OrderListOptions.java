/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.types.order;

import io.geekshop.types.common.ListOptions;
import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class OrderListOptions implements ListOptions {
    private Integer currentPage;
    private Integer pageSize;
    private OrderSortParameter sort;
    private OrderFilterParameter filter;
}
