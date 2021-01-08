/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.types.customer;

import io.geekshop.types.common.ListOptions;
import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class CustomerListOptions implements ListOptions {
    private Integer currentPage;
    private Integer pageSize;
    private CustomerSortParameter sort;
    private CustomerFilterParameter filter;
}
