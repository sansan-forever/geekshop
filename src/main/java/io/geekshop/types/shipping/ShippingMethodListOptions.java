/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.types.shipping;

import io.geekshop.types.common.ListOptions;
import lombok.Data;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Data
public class ShippingMethodListOptions implements ListOptions {
    private Integer currentPage;
    private Integer pageSize;
    private ShippingMethodSortParameter sort;
    private ShippingMethodFilterParameter filter;
}
