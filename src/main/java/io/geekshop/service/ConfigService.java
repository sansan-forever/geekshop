/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.service;

import io.geekshop.config.asset.AssetConfig;
import io.geekshop.config.auth.AuthConfig;
import io.geekshop.config.collection.CatalogConfig;
import io.geekshop.config.payment_method.PaymentOptions;
import io.geekshop.config.promotion.PromotionOptions;
import io.geekshop.config.shipping_method.ShippingOptions;
import io.geekshop.options.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Service
@Slf4j
public class ConfigService {
    private final ConfigOptions configOptions;
    @Autowired
    private AuthConfig authConfig;
    @Autowired
    private AssetConfig assetConfig;
    @Autowired
    private CatalogConfig catalogConfig;
    @Autowired
    private ShippingOptions shippingOptions;
    @Autowired
    private PaymentOptions paymentOptions;

    @Autowired
    private PromotionOptions promotionOptions;

    public ConfigService(ConfigOptions configOptions) {
        this.configOptions = configOptions;
        if (this.configOptions.getAuthOptions().isDisableAuth()) {
            log.warn("Auth has been disabled. This should never be the case for a production system!");
        }
    }

    public ShippingOptions getShippingOptions() {
        return this.shippingOptions;
    }

    public AuthOptions getAuthOptions() {
        return this.configOptions.getAuthOptions();
    }

    public AuthConfig getAuthConfig() {
        return this.authConfig;
    }

    public AssetOptions getAssetOptions() {
        return this.configOptions.getAssetOptions();
    }

    public AssetConfig getAssetConfig() {
        return assetConfig;
    }

    public CatalogConfig getCatalogConfig() {
        return catalogConfig;
    }

    public ImportExportOptions getImportExportOptions() { return this.configOptions.getImportExportOptions(); }

    public PaymentOptions getPaymentOptions() {
        return this.paymentOptions;
    }

    public PromotionOptions getPromotionOptions() {
        return this.promotionOptions;
    }

    public OrderOptions getOrderOptions() { return this.configOptions.getOrderOptions(); }
}
