/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.product;

import co.jueyi.geekshop.types.common.SortOrder;
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
