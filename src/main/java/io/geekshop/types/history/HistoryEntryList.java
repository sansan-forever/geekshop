/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.history;

import io.geekshop.types.common.PaginatedList;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class HistoryEntryList implements PaginatedList<HistoryEntry> {
    private List<HistoryEntry> items = new ArrayList<>();
    private Integer totalItems;
}
