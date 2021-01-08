/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.resolver.admin;

import io.geekshop.common.utils.BeanMapper;
import io.geekshop.custom.security.Allow;
import io.geekshop.entity.GlobalSettingsEntity;
import io.geekshop.service.GlobalSettingsService;
import io.geekshop.types.common.Permission;
import io.geekshop.types.settings.GlobalSettings;
import io.geekshop.types.settings.UpdateGlobalSettingsInput;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class GlobalSettingsMutation implements GraphQLMutationResolver {

    private final GlobalSettingsService globalSettingsService;

    @Allow(Permission.UpdateSettings)
    public GlobalSettings updateGlobalSettings(UpdateGlobalSettingsInput input, DataFetchingEnvironment dfe) {
        GlobalSettingsEntity globalSettingsEntity = globalSettingsService.updateSettings(input);
        return BeanMapper.map(globalSettingsEntity, GlobalSettings.class);
    }
}
