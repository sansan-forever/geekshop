/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.resolver;

import io.geekshop.entity.GlobalSettingsEntity;
import io.geekshop.service.ConfigService;
import io.geekshop.service.GlobalSettingsService;
import io.geekshop.service.OrderService;
import io.geekshop.types.settings.GlobalSettings;
import io.geekshop.types.settings.ServerConfig;
import graphql.kickstart.tools.GraphQLResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class GlobalSettingsResolver  implements GraphQLResolver<GlobalSettings> {

    private final GlobalSettingsService globalSettingsService;
    private final ConfigService configService;
    private final OrderService orderService;

    /**
     * Exposes a subset of the GeekShopConfig which may be of use to clients
     */
    public ServerConfig getServerConfig(GlobalSettings globalSettings) {
        ServerConfig serverConfig = new ServerConfig();
        GlobalSettingsEntity globalSettingsEntity = globalSettingsService.getSettings();
        serverConfig.getCustomFields().putAll(globalSettingsEntity.getCustomFields());
        serverConfig.setOrderProcess(orderService.getOrderProcessStates());
        List<String> permittedAssetTypes = configService.getAssetOptions().getPermittedFileTypes();
        serverConfig.getPermittedAssetTypes().addAll(permittedAssetTypes);
        return serverConfig;
    }
}
