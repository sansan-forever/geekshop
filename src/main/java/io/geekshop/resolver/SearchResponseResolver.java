/*
 * Copyright (c) 2021 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.resolver;

import io.geekshop.common.ApiType;
import io.geekshop.common.RequestContext;
import io.geekshop.service.SearchService;
import io.geekshop.types.search.FacetValueResult;
import io.geekshop.types.search.SearchResponse;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created on Jan, 2021 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class SearchResponseResolver implements GraphQLResolver<SearchResponse> {

    private final SearchService searchService;

    public List<FacetValueResult> getFacetValues(SearchResponse searchResponse, DataFetchingEnvironment dfe) {
        boolean publicOnly = false;
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        if (ApiType.SHOP.equals(ctx.getApiType())) {
            publicOnly = true;
        }

        return this.searchService.facetValues(searchResponse.getSearchInput(), publicOnly);
    }
}
