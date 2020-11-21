/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.types.settings.GlobalSettings;
import co.jueyi.geekshop.types.settings.UpdateGlobalSettingsInput;
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
    public GlobalSettings updateGlobalSettings(UpdateGlobalSettingsInput input, DataFetchingEnvironment dfe) {
        return null; // TODO
    }
}
