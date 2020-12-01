/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.config;

import co.jueyi.geekshop.ApiClient;
import co.jueyi.geekshop.MockDataService;
import co.jueyi.geekshop.TestHelper;
import co.jueyi.geekshop.config.asset.AssetConfig;
import co.jueyi.geekshop.config.asset.AssetPreviewStrategy;
import co.jueyi.geekshop.config.asset.AssetStorageStrategy;
import co.jueyi.geekshop.config.asset.DefaultAssetNamingStrategy;
import co.jueyi.geekshop.config.auth.AuthConfig;
import co.jueyi.geekshop.config.auth.AuthenticationStrategy;
import co.jueyi.geekshop.config.auth.NativeAuthenticationStrategy;
import co.jueyi.geekshop.config.auth.TestAuthenticationStrategy;
import co.jueyi.geekshop.config.session_cache.CachedSession;
import co.jueyi.geekshop.config.session_cache.SessionCacheStrategy;
import co.jueyi.geekshop.config.session_cache.TestingSessionCacheStrategy;
import co.jueyi.geekshop.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import javax.annotation.PostConstruct;
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

    @Autowired
    private ConfigService configService;

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

    @Bean
    @Primary
    public AssetStorageStrategy testAssetStorageStrategy() {
        return new TestAssetStorageStrategy();
    }

    @Bean
    @Primary
    public AssetPreviewStrategy testAssetPreviewStrategy() {
        return new TestAssetPreviewStrategy();
    }



    @PostConstruct
    void initTestConfig() {
        this.configService.getImportExportOptions().setImportAssetsDir(TestHelper.getImportAssetsDir());
    }
}
