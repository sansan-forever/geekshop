/*
 * Copyright (c) 2021 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.resolver.admin;

import io.geekshop.custom.security.Allow;
import io.geekshop.service.SearchService;
import io.geekshop.types.common.Permission;
import io.geekshop.types.common.SearchInput;
import io.geekshop.types.search.SearchResponse;
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
