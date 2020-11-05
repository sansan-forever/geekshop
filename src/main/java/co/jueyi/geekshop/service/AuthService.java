package co.jueyi.geekshop.service;

import co.jueyi.geekshop.common.ApiType;
import co.jueyi.geekshop.config.auth.AuthConfig;
import co.jueyi.geekshop.config.auth.AuthenticationStrategy;
import co.jueyi.geekshop.exception.InternalServerError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthConfig authConfig;

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
