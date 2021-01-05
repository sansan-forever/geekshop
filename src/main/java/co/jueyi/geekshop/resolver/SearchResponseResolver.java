/*
 * Copyright (c) 2021 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver;

import co.jueyi.geekshop.common.ApiType;
import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.service.SearchService;
import co.jueyi.geekshop.types.search.FacetValueResult;
import co.jueyi.geekshop.types.search.SearchResponse;
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
