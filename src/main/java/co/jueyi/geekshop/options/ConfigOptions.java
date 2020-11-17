/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.options;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created on Nov, 2020 by @author bobo
 */
@ConfigurationProperties(prefix = "geekshop")
@Data
public class ConfigOptions {
    /**
     * Configuration for authorization.
     */
    private AuthOptions authOptions = new AuthOptions();

    /**
     * Configuration for email.
     */
    private EmailOptions emailOptions = new EmailOptions();

    /**
     * Configuration for assets.
     */
    private AssetOptions assetOptions = new AssetOptions();
}
