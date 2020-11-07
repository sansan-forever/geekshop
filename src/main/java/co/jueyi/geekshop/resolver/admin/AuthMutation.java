package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.common.Constant;
import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.config.auth.NativeAuthenticationStrategy;
import co.jueyi.geekshop.custom.graphql.CustomGraphQLServletContext;
import co.jueyi.geekshop.custom.security.Allow;
import co.jueyi.geekshop.resolver.base.BaseAuthMutation;
import co.jueyi.geekshop.service.AdministratorService;
import co.jueyi.geekshop.service.AuthService;
import co.jueyi.geekshop.service.ConfigService;
import co.jueyi.geekshop.service.UserService;
import co.jueyi.geekshop.types.auth.AuthenticationInput;
import co.jueyi.geekshop.types.auth.LoginResult;
import co.jueyi.geekshop.types.common.Permission;
import com.google.common.collect.ImmutableMap;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public LoginResult login(String username, String password, Boolean rememberMe, DataFetchingEnvironment dfe) {
        AuthenticationInput input = new AuthenticationInput();
        input.setMethod(Constant.NATIVE_AUTH_STRATEGY_NAME);
        input.setData(ImmutableMap.of(
                NativeAuthenticationStrategy.KEY_USERNAME, username,
                NativeAuthenticationStrategy.KEY_PASSWORD, password));

        CustomGraphQLServletContext customGraphQLServletContext = dfe.getContext();
        RequestContext ctx = customGraphQLServletContext.getRequestContext();
        HttpServletRequest request = customGraphQLServletContext.getHttpServletRequest();
        HttpServletResponse response = customGraphQLServletContext.getHttpServletResponse();

        return super.authenticateAndCreateSession(ctx, input, rememberMe, request, response);
    }

    /**
     * Authenticates the user using a named authentication strategy
     */
    @Allow(Permission.Public)
    public LoginResult authenticate(AuthenticationInput input, Boolean rememberMe, DataFetchingEnvironment dfe) {
        CustomGraphQLServletContext customGraphQLServletContext = dfe.getContext();
        RequestContext ctx = customGraphQLServletContext.getRequestContext();
        HttpServletRequest request = customGraphQLServletContext.getHttpServletRequest();
        HttpServletResponse response = customGraphQLServletContext.getHttpServletResponse();

        return super.authenticateAndCreateSession(ctx, input, rememberMe, request, response);
    }

    @Allow(Permission.Public)
    public Boolean logout(DataFetchingEnvironment dfe) {
        CustomGraphQLServletContext customGraphQLServletContext = dfe.getContext();
        RequestContext ctx = customGraphQLServletContext.getRequestContext();
        HttpServletRequest request = customGraphQLServletContext.getHttpServletRequest();
        HttpServletResponse response = customGraphQLServletContext.getHttpServletResponse();
        return super.logout(ctx, request, response);
    }

}
