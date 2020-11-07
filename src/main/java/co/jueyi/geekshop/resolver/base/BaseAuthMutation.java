package co.jueyi.geekshop.resolver.base;

import co.jueyi.geekshop.common.ApiType;
import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.config.session_cache.CachedSession;
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
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created on Nov, 2020 by @author bobo
 */
@RequiredArgsConstructor
public abstract class BaseAuthMutation {
    protected final AuthService authService;
    protected final UserService userService;
    protected final AdministratorService administratorService;
    protected final ConfigService configService;

    /**
     * Attempts a login given the username and password of a user. If successful, returns
     * the user data and returns the token either in a cookie or in the response body.
     */
    protected Boolean logout(RequestContext ctx, HttpServletRequest request, HttpServletResponse response) {
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

    /**
     * Creates an authenticated session and sets the session token.
     */
    protected LoginResult authenticateAndCreateSession(
            RequestContext ctx,
            AuthenticationInput input,
            boolean rememberMe,
            HttpServletRequest request,
            HttpServletResponse response) {
        ApiType apiType = ctx.getApiType();
        CachedSession session = this.authService.authenticate(ctx, apiType, input.getMethod(), input.getData());
        if (apiType != null && ApiType.ADMIN.equals(apiType)) {
            Administrator administrator = this.administratorService.findOneByUserId(session.getUser().getId());
            if (administrator == null) {
                throw new UnauthorizedException();
            }
        }
        SessionTokenHelper.setSessionToken(session.getToken(),
                rememberMe,
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

}
