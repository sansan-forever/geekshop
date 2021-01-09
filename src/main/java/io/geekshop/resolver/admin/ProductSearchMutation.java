/*
 * Copyright (c) 2021 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.resolver.admin;

import io.geekshop.custom.security.Allow;
import io.geekshop.service.SearchService;
import io.geekshop.types.common.Permission;
import io.geekshop.types.search.SearchReindexResponse;
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
