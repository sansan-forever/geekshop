/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.product;

import io.geekshop.types.common.PaginatedList;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class ProductVariantList implements PaginatedList<ProductVariant> {
    private List<ProductVariant> items = new ArrayList<>();
    private Integer totalItems;
}
