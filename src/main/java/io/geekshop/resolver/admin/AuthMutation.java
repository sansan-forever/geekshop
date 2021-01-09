/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.resolver.admin;

import io.geekshop.custom.security.Allow;
import io.geekshop.resolver.base.BaseAuthMutation;
import io.geekshop.service.AdministratorService;
import io.geekshop.service.AuthService;
import io.geekshop.service.ConfigService;
import io.geekshop.service.UserService;
import io.geekshop.types.auth.AuthenticationInput;
import io.geekshop.types.auth.LoginResult;
import io.geekshop.types.common.Permission;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
public class AuthMutation extends BaseAuthMutation implements GraphQLMutationResolver {
    public AuthMutation(AuthService authService,
                        UserService userService,
                        AdministratorService administratorService,
                        ConfigService configService) {
        super(authService, userService, administratorService, configService);
    }

    /**
     * Authenticates the user using the native authentication strategy. This mutation is a alias for
     * `authenticate({ native: { ... }})`
     */
    @Allow(Permission.Public)
    public LoginResult adminLogin(String username, String password, Boolean rememberMe, DataFetchingEnvironment dfe) {
        return super.login(username, password, rememberMe, dfe);
    }

    /**
     * Authenticates the user using a named authentication strategy
     */
    @Allow(Permission.Public)
    public LoginResult adminAuthenticate(AuthenticationInput input, Boolean rememberMe, DataFetchingEnvironment dfe) {
        return super.authenticateAndCreateSession(input, rememberMe, dfe);
    }

    @Allow(Permission.Public)
    public Boolean adminLogout(DataFetchingEnvironment dfe) {
        return super.logout(dfe);
    }
}
