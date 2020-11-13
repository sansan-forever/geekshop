/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.service;

import co.jueyi.geekshop.config.auth.AuthConfig;
import co.jueyi.geekshop.options.AuthOptions;
import co.jueyi.geekshop.options.ConfigOptions;
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

    public ConfigService(ConfigOptions configOptions) {
        this.configOptions = configOptions;
        if (this.configOptions.getAuthOptions().isDisableAuth()) {
            log.warn("Auth has been disabled. This should never be the case for a production system!");
        }
    }

    public AuthOptions getAuthOptions() {
        return this.configOptions.getAuthOptions();
    }

    public AuthConfig getAuthConfig() {
        return this.authConfig;
    }


}
