/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.history;

import io.geekshop.types.common.ListOptions;
import lombok.Data;

import javax.validation.constraints.Min;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class HistoryEntryListOptions implements ListOptions {
    @Min(1)
    private Integer currentPage;
    @Min(1)
    private Integer pageSize;
    private HistoryEntrySortParameter sort;
    private HistoryEntryFilterParameter filter;
}
