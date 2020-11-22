/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.custom.security.Allow;
import co.jueyi.geekshop.entity.GlobalSettingsEntity;
import co.jueyi.geekshop.service.GlobalSettingsService;
import co.jueyi.geekshop.types.common.Permission;
import co.jueyi.geekshop.types.settings.GlobalSettings;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class GlobalSettingsQuery implements GraphQLQueryResolver {

    private final GlobalSettingsService globalSettingsService;

    @Allow(Permission.ReadSettings)
    public GlobalSettings globalSettings(DataFetchingEnvironment dfe) {
        GlobalSettingsEntity globalSettingsEntity = this.globalSettingsService.getSettings();
        return BeanMapper.map(globalSettingsEntity, GlobalSettings.class);
    }
}
