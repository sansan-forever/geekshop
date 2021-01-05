/*
 * Copyright (c) 2021 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.custom.security.Allow;
import co.jueyi.geekshop.service.SearchService;
import co.jueyi.geekshop.types.common.Permission;
import co.jueyi.geekshop.types.search.SearchReindexResponse;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created on Jan, 2021 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class ProductSearchMutation implements GraphQLMutationResolver {

    private final SearchService searchService;

    @Allow(Permission.UpdateCatalog)
    public SearchReindexResponse reindex(DataFetchingEnvironment dfe) {
        Boolean result = searchService.reindex();
        SearchReindexResponse reindexResponse = new SearchReindexResponse();
        reindexResponse.setSuccess(result);
        return reindexResponse;
    }
}
