/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.resolver.admin;

import io.geekshop.common.RequestContext;
import io.geekshop.custom.security.Allow;
import io.geekshop.resolver.base.BaseAuthQuery;
import io.geekshop.service.AdministratorService;
import io.geekshop.service.UserService;
import io.geekshop.types.auth.CurrentUser;
import io.geekshop.types.common.Permission;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
public class AuthQuery extends BaseAuthQuery implements GraphQLQueryResolver {
    public AuthQuery(
            AdministratorService administratorService, UserService userService) {
        super(administratorService, userService);
    }

    @Allow({Permission.Authenticated, Permission.Owner})
    public CurrentUser adminMe(DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        return super.me(ctx);
    }
}
