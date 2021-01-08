/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.resolver.admin;

import io.geekshop.common.utils.BeanMapper;
import io.geekshop.custom.security.Allow;
import io.geekshop.entity.AdministratorEntity;
import io.geekshop.service.AdministratorService;
import io.geekshop.types.administrator.Administrator;
import io.geekshop.types.administrator.CreateAdministratorInput;
import io.geekshop.types.administrator.UpdateAdministratorInput;
import io.geekshop.types.common.DeletionResponse;
import io.geekshop.types.common.Permission;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class AdministratorMutation implements GraphQLMutationResolver {

    private final AdministratorService administratorService;

    @Allow(Permission.CreateAdministrator)
    public Administrator createAdministrator(CreateAdministratorInput input, DataFetchingEnvironment dfe) {
        AdministratorEntity administratorEntity = this.administratorService.create(input);
        if (administratorEntity == null) return null;
        return BeanMapper.map(administratorEntity, Administrator.class);
    }

    @Allow(Permission.UpdateAdministrator)
    public Administrator updateAdministrator(UpdateAdministratorInput input, DataFetchingEnvironment dfe) {
        AdministratorEntity administratorEntity = this.administratorService.update(input);
        if (administratorEntity == null) return null;
        return BeanMapper.map(administratorEntity, Administrator.class);
    }

    @Allow(Permission.DeleteAdministrator)
    public DeletionResponse deleteAdministrator(Long id, DataFetchingEnvironment dfe) {
        return this.administratorService.softDelete(id);
    }

    @Allow(Permission.UpdateAdministrator)
    public Administrator assignRoleToAdministrator(Long administratorId, Long roleId, DataFetchingEnvironment dfe) {
        AdministratorEntity administratorEntity = this.administratorService.assignRole(administratorId, roleId);
        if (administratorEntity == null) return null;
        return BeanMapper.map(administratorEntity, Administrator.class);
    }
}
