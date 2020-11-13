/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.custom.security.Allow;
import co.jueyi.geekshop.entity.AdministratorEntity;
import co.jueyi.geekshop.service.AdministratorService;
import co.jueyi.geekshop.types.administrator.Administrator;
import co.jueyi.geekshop.types.administrator.CreateAdministratorInput;
import co.jueyi.geekshop.types.administrator.UpdateAdministratorInput;
import co.jueyi.geekshop.types.common.DeletionResponse;
import co.jueyi.geekshop.types.common.Permission;
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
