/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.history;

import io.geekshop.types.common.BooleanOperators;
import io.geekshop.types.common.DateOperators;
import io.geekshop.types.common.StringOperators;
import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class HistoryEntryFilterParameter {
    private DateOperators createdAt;
    private DateOperators updatedAt;
    private BooleanOperators isPublic;
    private StringOperators type;
}
