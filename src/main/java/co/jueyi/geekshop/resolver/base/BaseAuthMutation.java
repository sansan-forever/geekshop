package co.jueyi.geekshop.resolver.base;

import co.jueyi.geekshop.common.ApiType;
import co.jueyi.geekshop.common.Constant;
import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.config.auth.NativeAuthenticationStrategy;
import co.jueyi.geekshop.config.session_cache.CachedSession;
import co.jueyi.geekshop.custom.graphql.CustomGraphQLServletContext;
import co.jueyi.geekshop.custom.security.SessionTokenHelper;
import co.jueyi.geekshop.exception.InternalServerError;
import co.jueyi.geekshop.exception.UnauthorizedException;
import co.jueyi.geekshop.service.AdministratorService;
import co.jueyi.geekshop.service.AuthService;
import co.jueyi.geekshop.service.ConfigService;
import co.jueyi.geekshop.service.UserService;
import co.jueyi.geekshop.types.administrator.Administrator;
import co.jueyi.geekshop.types.auth.AuthenticationInput;
import co.jueyi.geekshop.types.auth.CurrentUser;
import co.jueyi.geekshop.types.auth.LoginResult;
import com.google.common.collect.ImmutableMap;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Slf4j
public abstract class BaseAuthMutation {
    protected final AuthService authService;
    protected final UserService userService;
    protected final AdministratorService administratorService;
    protected final ConfigService configService;
    protected final boolean nativeAuthStrategyIsConfigured;

    protected BaseAuthMutation(
            AuthService authService,
            UserService userService,
            AdministratorService administratorService,
            ConfigService configService) {
        this.authService = authService;
        this.userService = userService;
        this.administratorService = administratorService;
        this.configService = configService;
        this.nativeAuthStrategyIsConfigured =
                this.configService.getAuthConfig().getShopAuthenticationStrategy()
                        .stream().anyMatch(strategy -> Constant.NATIVE_AUTH_STRATEGY_NAME.equals(strategy.getName()));
    }

    /**
     * Attempts a login given the username and password of a user. If successful, returns
     * the user data and returns the token either in a cookie or in the response body.
     */
    protected Boolean logout(DataFetchingEnvironment dfe) {

        CustomGraphQLServletContext customGraphQLServletContext = dfe.getContext();
        RequestContext ctx = customGraphQLServletContext.getRequestContext();
        HttpServletRequest request = customGraphQLServletContext.getHttpServletRequest();
        HttpServletResponse response = customGraphQLServletContext.getHttpServletResponse();

        String token = SessionTokenHelper.extractSessionToken(
                request,
                this.configService.getAuthOptions().getTokenMethod());
        if (StringUtils.isEmpty(token)) return false;

        this.authService.destroyAuthenticatedSession(ctx, token);

        SessionTokenHelper.setSessionToken(
                "",
                false,
                this.configService.getAuthOptions(),
                request,
                response
        );
        return true;
    }

    protected LoginResult login(String username, String password, Boolean rememberMe, DataFetchingEnvironment dfe) {
        AuthenticationInput input = new AuthenticationInput();
        input.setMethod(Constant.NATIVE_AUTH_STRATEGY_NAME);
        input.setData(ImmutableMap.of(
                NativeAuthenticationStrategy.KEY_USERNAME, username,
                NativeAuthenticationStrategy.KEY_PASSWORD, password));

        LoginResult loginResult = this.authenticateAndCreateSession(input, rememberMe, dfe);
        return loginResult;
    }

    /**
     * Creates an authenticated session and sets the session token.
     */
    protected LoginResult authenticateAndCreateSession(
            AuthenticationInput input,
            Boolean rememberMe,
            DataFetchingEnvironment dfe) {

        CustomGraphQLServletContext customGraphQLServletContext = dfe.getContext();
        RequestContext ctx = customGraphQLServletContext.getRequestContext();
        HttpServletRequest request = customGraphQLServletContext.getHttpServletRequest();
        HttpServletResponse response = customGraphQLServletContext.getHttpServletResponse();

        ApiType apiType = ctx.getApiType();
        CachedSession session = this.authService.authenticate(ctx, apiType, input.getMethod(), input.getData());
        if (apiType != null && ApiType.ADMIN.equals(apiType)) {
            Administrator administrator = this.administratorService.findOneByUserId(session.getUser().getId());
            if (administrator == null) {
                throw new UnauthorizedException();
            }
        }
        SessionTokenHelper.setSessionToken(session.getToken(),
                BooleanUtils.toBoolean(rememberMe),
                this.configService.getAuthOptions(),
                request,
                response);
        LoginResult loginResult = new LoginResult();
        CurrentUser currentUser = BeanMapper.map(session.getUser(), CurrentUser.class);
        loginResult.setUser(currentUser);
        return loginResult;
    }

    /**
     * Update the password of an existing User.
     */
    protected boolean updatePassword(RequestContext ctx, String currentPassword, String newPassword) {
        Long activeUserId = ctx.getActiveUserId();
        if (activeUserId == null) {
            throw new InternalServerError("No active user");
        }
        return this.userService.updatePassword(activeUserId, currentPassword, newPassword);
    }

    protected void requireNativeAuthStrategy() {
        if (!this.nativeAuthStrategyIsConfigured) {
            String authStrategyNames = this.configService.getAuthConfig().getShopAuthenticationStrategy()
                    .stream().map(strategy -> strategy.getName()).collect(Collectors.joining(", "));
            String errorMessage =
                    "This GraphQL operation requires that the NativeAuthenticationStrategy be configured " +
                            "for the Shop API.\n" +
                            "Currently the following AuthenticationStrategies are enabled: " + authStrategyNames;
            log.error(errorMessage);
            throw new InternalServerError("Server Configuration Error");
        }
    }

}
