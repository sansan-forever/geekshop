/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.product;

import io.geekshop.types.common.SortOrder;
import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class ProductVariantSortParameter {
    private SortOrder stockOnHand;
    private SortOrder id;
    private SortOrder productId;
    private SortOrder createdAt;
    private SortOrder updatedAt;
    private SortOrder sku;
    private SortOrder name;
    private SortOrder price;
}
