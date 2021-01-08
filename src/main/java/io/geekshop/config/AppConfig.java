/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.config;

import io.geekshop.common.Constant;
import io.geekshop.common.RequestContext;
import io.geekshop.common.utils.IdUtil;
import io.geekshop.config.asset.*;
import io.geekshop.config.auth.AuthConfig;
import io.geekshop.config.auth.AuthenticationStrategy;
import io.geekshop.config.auth.NativeAuthenticationStrategy;
import io.geekshop.config.collection.CatalogConfig;
import io.geekshop.config.collection.CollectionFilter;
import io.geekshop.config.collection.FacetValueCollectionFilter;
import io.geekshop.config.collection.VariantNameCollectionFilter;
import io.geekshop.config.order.MergeOrdersStrategy;
import io.geekshop.config.order.OrderCodeGenerator;
import io.geekshop.config.order.OrderMergeOptions;
import io.geekshop.config.order.UseGuestStrategy;
import io.geekshop.config.payment_method.ExamplePaymentMethodHandler;
import io.geekshop.config.payment_method.PaymentOptions;
import io.geekshop.config.promotion.PromotionAction;
import io.geekshop.config.promotion.PromotionCondition;
import io.geekshop.config.promotion.PromotionOptions;
import io.geekshop.config.promotion.actions.FacetValuesDiscountAction;
import io.geekshop.config.promotion.actions.OrderPercentageDiscount;
import io.geekshop.config.promotion.actions.ProductDiscountAction;
import io.geekshop.config.promotion.conditions.ContainsProductsCondition;
import io.geekshop.config.promotion.conditions.CustomerGroupCondition;
import io.geekshop.config.promotion.conditions.HasFacetValuesCondition;
import io.geekshop.config.promotion.conditions.MinimumOrderAmountCondition;
import io.geekshop.config.session_cache.InMemorySessionCacheStrategy;
import io.geekshop.config.session_cache.SessionCacheStrategy;
import io.geekshop.config.shipping_method.*;
import io.geekshop.email.EmailSender;
import io.geekshop.email.FileEmailSender;
import io.geekshop.email.NoopEmailSender;
import io.geekshop.email.SmtpEmailSender;
import io.geekshop.options.ConfigOptions;
import io.geekshop.service.helpers.search_strategy.DbSearchStrategy;
import io.geekshop.service.helpers.search_strategy.SearchStrategy;
import io.geekshop.service.helpers.search_strategy.SearchStrategyUtils;
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

import java.util.*;

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

    /**
     * Configures the available checkers and calculators for ShippingMethods.
     */
    @Bean
    public ShippingOptions shippingOptions() {
        return new ShippingOptions(shippingEligibilityCheckers(), shippingCalculators());
    }

    /**
     * A List of available ShippingEligibilityCheckers for use in configuring ShippingMethods
     * Autowired in {@link ShippingOptions}
     */
    @Bean
    public List<ShippingEligibilityChecker> shippingEligibilityCheckers() {
        return Arrays.asList(defaultShippingEligibilityChecker());
    }

    /**
     * A List of available ShippingCalculators for use in configuring ShippingMethods
     * Autowired in {@link ShippingOptions}
     */
    @Bean
    public List<ShippingCalculator> shippingCalculators() {
        return Arrays.asList(defaultShippingCalculator());
    }

    @Bean
    public ShippingEligibilityChecker defaultShippingEligibilityChecker() {
        return new DefaultShippingEligibilityChecker();
    }

    @Bean
    public ShippingCalculator defaultShippingCalculator() {
        return new DefaultShippingCalculator();
    }

    @Bean
    HasFacetValuesCondition hasFacetValuesCondition() {
        return new HasFacetValuesCondition();
    }

    @Bean
    CustomerGroupCondition customerGroupCondition() {
        return new CustomerGroupCondition();
    }

    @Bean
    ContainsProductsCondition containsProductsCondition() {
        return new ContainsProductsCondition();
    }

    @Bean
    MinimumOrderAmountCondition minimumOrderAmountCondition() {
        return new MinimumOrderAmountCondition();
    }

    @Bean
    public List<PromotionCondition> defaultPromotionConditions() {
        return Arrays.asList(
                minimumOrderAmountCondition(),
                hasFacetValuesCondition(),
                containsProductsCondition(),
                customerGroupCondition()
        );
    }

    @Bean
    OrderPercentageDiscount orderPercentageDiscount() {
        return new OrderPercentageDiscount();
    }

    @Bean
    FacetValuesDiscountAction facetValuesDiscountAction() {
        return new FacetValuesDiscountAction();
    }

    @Bean
    ProductDiscountAction productDiscountAction() {
        return new ProductDiscountAction();
    }

    @Bean
    public List<PromotionAction> defaultPromotionActions() {
        return Arrays.asList(
                orderPercentageDiscount(),
                facetValuesDiscountAction(),
                productDiscountAction()
        );
    }

    /**
     * Configures the Conditions and Actions available when creating Promotions.
     */
    @Bean
    public PromotionOptions promotionOptions() {
        return new PromotionOptions(
                defaultPromotionConditions(),
                defaultPromotionActions()
        );
    }

    /**
     * Configures available payment processing methods.
     */
    @Bean
    public PaymentOptions paymentOptions() {
        return new PaymentOptions(
                Arrays.asList(examplePaymentMethodHandler())
        );
    }

    @Bean
    ExamplePaymentMethodHandler examplePaymentMethodHandler() {
        return new ExamplePaymentMethodHandler();
    }

    @Bean
    public OrderMergeOptions orderMergeOptions() {
        return new OrderMergeOptions(
                new MergeOrdersStrategy(),
                new UseGuestStrategy()
        );
    }

    @Bean
    public OrderCodeGenerator orderCodeGenerator() {
        return (RequestContext ctx) -> IdUtil.generatePublicId();
    }

    @Bean
    public SearchStrategy searchStrategy() {
        return new DbSearchStrategy();
    }
}
