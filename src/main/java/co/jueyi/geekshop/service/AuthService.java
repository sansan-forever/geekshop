/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.service;

import co.jueyi.geekshop.common.ApiType;
import co.jueyi.geekshop.common.Constant;
import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.config.auth.AuthConfig;
import co.jueyi.geekshop.config.auth.AuthenticationStrategy;
import co.jueyi.geekshop.config.auth.NativeAuthenticationStrategy;
import co.jueyi.geekshop.config.session_cache.CachedSession;
import co.jueyi.geekshop.entity.SessionEntity;
import co.jueyi.geekshop.eventbus.events.AttemptedLoginEvent;
import co.jueyi.geekshop.eventbus.events.LoginEvent;
import co.jueyi.geekshop.eventbus.events.LogoutEvent;
import co.jueyi.geekshop.exception.InternalServerError;
import co.jueyi.geekshop.exception.NotVerifiedException;
import co.jueyi.geekshop.exception.UnauthorizedException;
import co.jueyi.geekshop.mapper.SessionEntityMapper;
import co.jueyi.geekshop.options.ConfigOptions;
import co.jueyi.geekshop.types.user.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.eventbus.EventBus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthConfig authConfig;
    private final ConfigOptions configOptions;
    private final UserService userService;
    private final SessionService sessionService;
    private final SessionEntityMapper sessionEntityMapper;
    private final EventBus eventBus;

    /**
     * Authenticates a user's credentials and if okay, creates a new session.
     */
    public CachedSession authenticate(
            RequestContext ctx,
            ApiType apiType,
            String authMethod,
            Map<String, String> inputMap) {
        AttemptedLoginEvent attemptedLoginEvent = new AttemptedLoginEvent(
                ctx,
                authMethod,
                Constant.NATIVE_AUTH_STRATEGY_NAME.equals(authMethod)
                        ? inputMap.get(NativeAuthenticationStrategy.KEY_USERNAME) : null
        );
        this.eventBus.post(attemptedLoginEvent);

        AuthenticationStrategy authStrategy = this.getAuthenticationStrategy(apiType, authMethod);
        User user = authStrategy.authenticate(ctx, authStrategy.convertInputToData(inputMap));
        if (user == null) throw new UnauthorizedException();
        return this.createAuthenticatedSessionForUser(ctx, user, authStrategy.getName());
    }


    public CachedSession createAuthenticatedSessionForUser(
            RequestContext ctx, User user, String authenticationStrategyName) {
        if (CollectionUtils.isEmpty(user.getRoles())) {
            user.setRoles(userService.findRolesByUserId(user.getId()));
        }

        if (configOptions.getAuthOptions().isRequireVerification() && !user.getVerified()) {
            throw new NotVerifiedException();
        }

        if (ctx.getSession() != null && ctx.getSession().getActiveOrderId() != null) {
            this.sessionService.deleteSessionsByActiveOrderId(ctx.getSession().getActiveOrderId());
        }
        user.setLastLogin(new Date());
        CachedSession session = sessionService.createNewAuthenticatedSession(
                ctx, user, authenticationStrategyName);
        this.eventBus.post(new LoginEvent(ctx, user));
        return session;
    }

    /**
     * Verify the provided password against the one we have for the given user.
     */
    public boolean verifyUserPassword(Long userId, String password) {
        NativeAuthenticationStrategy nativeAuthStrategy =
                (NativeAuthenticationStrategy) this.getAuthenticationStrategy(
                        ApiType.SHOP, Constant.NATIVE_AUTH_STRATEGY_NAME);
        boolean passwordMatches = nativeAuthStrategy.verifyUserPassword(userId, password);
        if (!passwordMatches) {
            throw new UnauthorizedException();
        }
        return true;
    }

    /**
     * Deletes all sessions for the user associated with the given session token
     */
    public void destroyAuthenticatedSession(RequestContext ctx, String sessionToken) {
        QueryWrapper<SessionEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SessionEntity::getToken, sessionToken).eq(SessionEntity::isAnonymous, false);
        SessionEntity session = this.sessionEntityMapper.selectOne(queryWrapper);
        if (session != null) {
            AuthenticationStrategy authenticationStrategy = this.getAuthenticationStrategy(
                    ctx.getApiType(),
                    session.getAuthenticationStrategy()
            );
            User user = this.userService.findUserWithRolesById(session.getUserId());
            authenticationStrategy.onLogOut(user);
            this.eventBus.post(new LogoutEvent(ctx));
            this.sessionService.deleteSessionByUserId(user.getId());
        }
    }

    private AuthenticationStrategy getAuthenticationStrategy(ApiType apiType, String method) {
        List<AuthenticationStrategy> strategies = ApiType.ADMIN.equals(apiType)
                ? authConfig.getAdminAuthenticationStrategy()
                : authConfig.getShopAuthenticationStrategy();

        AuthenticationStrategy match = strategies.stream().filter(s -> s.getName().equals(method))
                .findFirst().orElse(null);
        if (match == null) {
            throw new InternalServerError("Unrecognized authentication strategy");
        }
        return match;
    }
}
