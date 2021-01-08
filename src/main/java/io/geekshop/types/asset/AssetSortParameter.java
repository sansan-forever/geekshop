/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.types.asset;

import io.geekshop.types.common.SortOrder;
import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class AssetSortParameter {
    private SortOrder id;
    private SortOrder name;
    private SortOrder createdAt;
    private SortOrder updatedAt;
}
