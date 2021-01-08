/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.resolver.shop;

import io.geekshop.common.RequestContext;
import io.geekshop.custom.security.Allow;
import io.geekshop.resolver.base.BaseAuthQuery;
import io.geekshop.service.AdministratorService;
import io.geekshop.service.UserService;
import io.geekshop.types.auth.CurrentUser;
import io.geekshop.types.common.Permission;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@Slf4j
public class ShopAuthQuery extends BaseAuthQuery implements GraphQLQueryResolver {

    public ShopAuthQuery(
            AdministratorService administratorService, UserService userService) {
        super(administratorService, userService);
    }

    /**
     * Returns information about the current authenticated User
     */
    @Allow(Permission.Authenticated)
    public CurrentUser me(DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        return super.me(ctx);
    }

}
