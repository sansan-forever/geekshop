/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.asset;

import co.jueyi.geekshop.types.common.SortOrder;
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
