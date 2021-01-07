/*
 * Copyright (c) 2021 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.custom.security.Allow;
import co.jueyi.geekshop.service.SearchService;
import co.jueyi.geekshop.types.common.Permission;
import co.jueyi.geekshop.types.common.SearchInput;
import co.jueyi.geekshop.types.search.SearchResponse;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created on Jan, 2021 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class ProductSearchQuery implements GraphQLQueryResolver {

    private final SearchService searchService;

    @Allow(Permission.ReadCatalog)
    public SearchResponse searchByAdmin(SearchInput input, DataFetchingEnvironment dfe) {
        SearchResponse searchResponse = this.searchService.search(input);
        searchResponse.setSearchInput(input);
        return searchResponse;
    }
}
