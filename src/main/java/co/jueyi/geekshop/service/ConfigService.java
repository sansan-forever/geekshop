/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.service;

import co.jueyi.geekshop.config.asset.AssetConfig;
import co.jueyi.geekshop.config.auth.AuthConfig;
import co.jueyi.geekshop.config.collection.CatalogConfig;
import co.jueyi.geekshop.config.payment_method.PaymentOptions;
import co.jueyi.geekshop.config.promotion.PromotionOptions;
import co.jueyi.geekshop.config.shipping_method.ShippingOptions;
import co.jueyi.geekshop.options.AssetOptions;
import co.jueyi.geekshop.options.AuthOptions;
import co.jueyi.geekshop.options.ConfigOptions;
import co.jueyi.geekshop.options.ImportExportOptions;
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
}
