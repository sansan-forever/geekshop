/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.common;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class SearchInput {
    private String term;
    private List<Long> facetValueIds = new ArrayList<>();
    private LogicalOperator facetValueOperator;
    private Long collectionId;
    private String collectionSlug;
    private Integer currentPage;
    private Integer pageSize;
    private SearchResultSortParameter sort;
}
