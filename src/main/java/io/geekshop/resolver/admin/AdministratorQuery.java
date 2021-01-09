/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.resolver.admin;

import io.geekshop.common.utils.BeanMapper;
import io.geekshop.custom.security.Allow;
import io.geekshop.entity.AdministratorEntity;
import io.geekshop.service.AdministratorService;
import io.geekshop.types.administrator.Administrator;
import io.geekshop.types.administrator.AdministratorList;
import io.geekshop.types.administrator.AdministratorListOptions;
import io.geekshop.types.common.Permission;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class AdministratorQuery implements GraphQLQueryResolver {

    private final AdministratorService administratorService;

    @Allow(Permission.ReadAdministrator)
    public AdministratorList administrators(AdministratorListOptions options, DataFetchingEnvironment dfe) {
        return administratorService.findAll(options);
    }

    @Allow(Permission.ReadAdministrator)
    public Administrator administrator(Long id, DataFetchingEnvironment dfe) {
        AdministratorEntity administratorEntity = administratorService.findOneEntity(id);
        if (administratorEntity == null) return null;
        return BeanMapper.map(administratorEntity, Administrator.class);
    }
}
