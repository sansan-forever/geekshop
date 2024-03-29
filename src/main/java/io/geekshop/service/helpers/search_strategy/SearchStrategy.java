/*
 * Copyright (c) 2021 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.service.helpers.search_strategy;

import io.geekshop.types.common.SearchInput;
import io.geekshop.types.search.SearchResult;

import java.util.List;
import java.util.Map;

/**
 * This interface defines the contract that any database-specific search implementations should follow.
 *
 * Created on Jan, 2021 by @author bobo
 */
public interface SearchStrategy {
    List<SearchResult> getSearchResults(SearchInput input, boolean enabledOnly);
    Integer getTotalCount(SearchInput input, boolean enabledOnly);
    /**
     * Returns a map of `facetValueId` => `count`, providing the number of times that
     * facetValue occurs in the result set.
     */
    Map<Long, Integer> getFacetValueIds(SearchInput input, boolean enabledOnly);
}
