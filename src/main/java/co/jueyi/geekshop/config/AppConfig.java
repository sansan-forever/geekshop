/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.config;

import co.jueyi.geekshop.common.Constant;
import co.jueyi.geekshop.config.asset.*;
import co.jueyi.geekshop.config.auth.AuthConfig;
import co.jueyi.geekshop.config.auth.AuthenticationStrategy;
import co.jueyi.geekshop.config.auth.NativeAuthenticationStrategy;
import co.jueyi.geekshop.config.collection.CatalogConfig;
import co.jueyi.geekshop.config.collection.CollectionFilter;
import co.jueyi.geekshop.config.collection.FacetValueCollectionFilter;
import co.jueyi.geekshop.config.collection.VariantNameCollectionFilter;
import co.jueyi.geekshop.config.session_cache.InMemorySessionCacheStrategy;
import co.jueyi.geekshop.config.session_cache.SessionCacheStrategy;
import co.jueyi.geekshop.email.EmailSender;
import co.jueyi.geekshop.email.FileEmailSender;
import co.jueyi.geekshop.email.NoopEmailSender;
import co.jueyi.geekshop.email.SmtpEmailSender;
import co.jueyi.geekshop.options.ConfigOptions;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ClasspathLoader;
import com.mitchellbosecke.pebble.loader.Loader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.concurrent.Executor;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Configuration
public class AppConfig {
    @Autowired
    private ConfigOptions configOptions;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public EventBus eventBus() {
        return new AsyncEventBus(asyncExecutor());
    }

    @Bean
    public Loader templateLoader() {
        ClasspathLoader loader = new ClasspathLoader();
        loader.setPrefix(Constant.PEBBLE_TEMPLATE_PREFIX);
        loader.setSuffix(Constant.PEBBLE_TEMPLATE_SUFFIX);
        return loader;
    }

    @Bean
    public PebbleEngine emailPebbleEngine() {
        return new PebbleEngine.Builder()
                .loader(this.templateLoader())
                .build();
    }

    @Bean
    public ThreadPoolTaskExecutor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(100);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("AsyncThread-");
        executor.initialize();
        return executor;
    }

    @Bean
    @ConditionalOnProperty(value = "geekshop.email-options.transport", havingValue = "file")
    public FileEmailSender fileEmailSender() {
        return new FileEmailSender(this.configOptions.getEmailOptions().getOutputPath());
    }

    @Bean
    @ConditionalOnProperty(value = "geekshop.email-options.transport", havingValue = "smtp")
    public SmtpEmailSender smtpEmailSender(JavaMailSender javaMailSender) {
        return new SmtpEmailSender(javaMailSender);
    }

    @Bean
    @ConditionalOnMissingBean(EmailSender.class)
    public NoopEmailSender noopEmailSender() {
        return new NoopEmailSender();
    }

    @Bean
    public AuthConfig authConfig() {
        // sessionCacheStrategy autowired
        return new AuthConfig(
                Arrays.asList(nativeAuthStrategy()),
                Arrays.asList(nativeAuthStrategy())
        );
    }

    @Bean
    public SessionCacheStrategy sessionCacheStrategy() {
        return new InMemorySessionCacheStrategy();
    }

    @Bean
    public AuthenticationStrategy nativeAuthStrategy() {
        return new NativeAuthenticationStrategy();
    }

    @Bean
    public AssetNamingStrategy assetNamingStrategy() {
        return new DefaultAssetNamingStrategy();
    }

    @Bean
    public AssetStorageStrategy assetStorageStrategy() {
        return new NoAssetStorageStrategy();
    }

    @Bean
    public AssetPreviewStrategy assetPreviewStrategy() {
        return new NoAssetPreviewStrategy();
    }

    @Bean
    public CatalogConfig catalogConfig() {
        return new CatalogConfig(
                Arrays.asList(facetValueCollectionFilter(), variantNameCollectionFilter())
        );
    }

    @Bean
    public CollectionFilter facetValueCollectionFilter() {
        return new FacetValueCollectionFilter();
    }

    @Bean
    public CollectionFilter variantNameCollectionFilter() {
        return new VariantNameCollectionFilter();
    }
}
