package co.jueyi.geekshop.config.auth;

import co.jueyi.geekshop.config.session_cache.SessionCacheStrategy;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * The AuthConfig define how auth & auth is managed.
 *
 * Created on Nov, 2020 by @author bobo
 */
@Data
@RequiredArgsConstructor
public class AuthConfig {
    /**
     * Configures one or more AuthenticationStrategies which defines how authentication
     * is handled in the Shop API.
     *
     * @default NativeAuthenticationStrategy
     */
    private final List<AuthenticationStrategy> shopAuthenticationStrategy;
    /**
     * Configures one or more AuthenticationStrategies which defines how authentication
     * is handled in the Admin API.
     */
    private final List<AuthenticationStrategy> adminAuthenticationStrategy;
    /**
     * This strategy defines how sessions will be cached. By default, sessions are cached using a simple
     * in-memory caching strategy which is suitable for development and low-traffic, single-instance
     * deployments.
     */
    private final SessionCacheStrategy sessionCacheStrategy;
}
