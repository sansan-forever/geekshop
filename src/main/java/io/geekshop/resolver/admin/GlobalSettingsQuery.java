/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.resolver.admin;

import io.geekshop.common.utils.BeanMapper;
import io.geekshop.custom.security.Allow;
import io.geekshop.entity.GlobalSettingsEntity;
import io.geekshop.service.GlobalSettingsService;
import io.geekshop.types.common.Permission;
import io.geekshop.types.settings.GlobalSettings;
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
