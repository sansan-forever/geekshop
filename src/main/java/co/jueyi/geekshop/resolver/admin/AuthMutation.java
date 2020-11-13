/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.custom.security.Allow;
import co.jueyi.geekshop.resolver.base.BaseAuthMutation;
import co.jueyi.geekshop.service.AdministratorService;
import co.jueyi.geekshop.service.AuthService;
import co.jueyi.geekshop.service.ConfigService;
import co.jueyi.geekshop.service.UserService;
import co.jueyi.geekshop.types.auth.AuthenticationInput;
import co.jueyi.geekshop.types.auth.LoginResult;
import co.jueyi.geekshop.types.common.Permission;
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
