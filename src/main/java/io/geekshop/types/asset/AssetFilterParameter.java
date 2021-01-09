/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.asset;

import io.geekshop.types.common.DateOperators;
import io.geekshop.types.common.StringOperators;
import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class AssetFilterParameter {
    private StringOperators name;
    private StringOperators type;
    private DateOperators createdAt;
    private DateOperators updatedAt;
}
