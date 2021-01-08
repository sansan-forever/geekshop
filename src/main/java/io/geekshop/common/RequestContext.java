/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.common;

import io.geekshop.config.session_cache.CachedSession;
import io.geekshop.custom.graphql.CustomGraphQLServletContext;
import graphql.schema.DataFetchingEnvironment;
import lombok.Data;

/**
 * The RequestContext holds information relevant to the current request, which may be
 * required at various points of the stack.
 *
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class RequestContext {
    private CachedSession session;
    private boolean authorized;
    private boolean authorizedAsOwnerOnly;
    private ApiType apiType;

    public Long getActiveUserId() {
        if (session == null || session.getUser()== null) return null;
        return session.getUser().getId();
    }

    public static RequestContext fromDataFetchingEnvironment(DataFetchingEnvironment dfe) {
        CustomGraphQLServletContext graphQLServletContext = dfe.getContext();
        return graphQLServletContext.getRequestContext();
    }
}
