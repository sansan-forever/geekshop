package co.jueyi.geekshop.config;

import co.jueyi.geekshop.ApiClient;
import co.jueyi.geekshop.MockDataService;
import co.jueyi.geekshop.config.auth.AuthConfig;
import co.jueyi.geekshop.config.auth.AuthenticationStrategy;
import co.jueyi.geekshop.config.auth.NativeAuthenticationStrategy;
import co.jueyi.geekshop.config.auth.TestAuthenticationStrategy;
import co.jueyi.geekshop.config.session_cache.CachedSession;
import co.jueyi.geekshop.config.session_cache.SessionCacheStrategy;
import co.jueyi.geekshop.config.session_cache.TestingSessionCacheStrategy;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Configuration
public class TestConfig {
    public static final long ASYNC_TIMEOUT = 5000;

    public static final String ADMIN_CLIENT_BEAN = "adminClient";
    public static final String SHOP_CLIENT_BEAN = "shopClient";

    @Bean(name=ADMIN_CLIENT_BEAN)
    public ApiClient adminClient() {
        return new ApiClient(true);
    }

    @Bean(name=SHOP_CLIENT_BEAN)
    public ApiClient shopClient() {
        return new ApiClient(false);
    }

    @Bean
    public MockDataService mockDataService() {
        return new MockDataService();
    }

    @Bean
    public RestTemplateCustomizer restTemplateCustomizer() {
        return restTemplate -> {
            restTemplate.setRequestFactory(clientHttpRequestFactory());
        };
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        // 延长超时用于调试
        clientHttpRequestFactory.setConnectTimeout(100000);
        clientHttpRequestFactory.setReadTimeout(100000);
        clientHttpRequestFactory.setBufferRequestBody(false);
        return clientHttpRequestFactory;
    }

    @Bean
    @Primary
    public AuthConfig testAuthConfig() {
        return new AuthConfig(
                Arrays.asList(testNativeAuthStrategy(), testAuthStrategy()),
                Arrays.asList(testNativeAuthStrategy())
        );
    }

    @Bean
    public AuthenticationStrategy testNativeAuthStrategy() {
        return new NativeAuthenticationStrategy();
    }

    @Bean
    public AuthenticationStrategy testAuthStrategy() {
        return new TestAuthenticationStrategy();
    }

    @Bean
    @Primary
    public SessionCacheStrategy testSessionCacheStrategy() {
        return new TestingSessionCacheStrategy(testSessionCache());
    }

    @Bean
    public Map<String, CachedSession> testSessionCache() {
        return new HashMap<>();
    }
}
