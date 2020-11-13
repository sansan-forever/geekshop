/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver;

import co.jueyi.geekshop.common.Constant;
import co.jueyi.geekshop.types.role.Role;
import co.jueyi.geekshop.types.user.AuthenticationMethod;
import co.jueyi.geekshop.types.user.User;
import graphql.kickstart.execution.context.GraphQLContext;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
public class UserResolver implements GraphQLResolver<User> {
    public CompletableFuture<List<Role>> getRoles(User user, DataFetchingEnvironment dfe) {
        final DataLoader<Long, List<Role>> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_USER_ROLES);

        return dataLoader.load(user.getId());
    }

    public CompletableFuture<List<AuthenticationMethod>> getAuthenticationMethods(
            User user, DataFetchingEnvironment dfe) {
        final DataLoader<Long, List<AuthenticationMethod>> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_USER_AUTHENTICATION_METHODS);

        return dataLoader.load(user.getId());
    }
}
