/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.collection;

import io.geekshop.types.common.BooleanOperators;
import io.geekshop.types.common.DateOperators;
import io.geekshop.types.common.NumberOperators;
import io.geekshop.types.common.StringOperators;
import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class CollectionFilterParameter {
    private BooleanOperators privateOnly;
    private DateOperators createdAt;
    private DateOperators updatedAt;
    private StringOperators name;
    private StringOperators slug;
    private NumberOperators position;
    private StringOperators description;
}
