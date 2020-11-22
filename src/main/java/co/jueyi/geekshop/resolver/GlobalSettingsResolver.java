/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver;

import co.jueyi.geekshop.entity.GlobalSettingsEntity;
import co.jueyi.geekshop.service.ConfigService;
import co.jueyi.geekshop.service.GlobalSettingsService;
import co.jueyi.geekshop.types.settings.GlobalSettings;
import co.jueyi.geekshop.types.settings.ServerConfig;
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

    /**
     * Exposes a subset of the GeekShopConfig which may be of use to clients
     */
    public ServerConfig getServerConfig(GlobalSettings globalSettings) {
        ServerConfig serverConfig = new ServerConfig();
        GlobalSettingsEntity globalSettingsEntity = globalSettingsService.getSettings();
        serverConfig.getCustomFields().putAll(globalSettingsEntity.getCustomFields());
        // TODO setting orderProcess
        List<String> permittedAssetTypes = configService.getAssetOptions().getPermittedFileTypes();
        serverConfig.getPermittedAssetTypes().addAll(permittedAssetTypes);
        return serverConfig;
    }
}
