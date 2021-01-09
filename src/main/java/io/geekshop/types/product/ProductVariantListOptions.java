/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.product;

import io.geekshop.types.common.ListOptions;
import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class ProductVariantListOptions implements ListOptions {
    private Integer currentPage;
    private Integer pageSize;
    private ProductVariantSortParameter sort;
    private ProductVariantFilterParameter filter;
}
