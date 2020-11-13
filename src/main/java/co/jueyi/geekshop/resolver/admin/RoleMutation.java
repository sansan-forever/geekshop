/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.custom.security.Allow;
import co.jueyi.geekshop.service.RoleService;
import co.jueyi.geekshop.types.common.DeletionResponse;
import co.jueyi.geekshop.types.common.Permission;
import co.jueyi.geekshop.types.role.*;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class RoleMutation implements GraphQLMutationResolver {

    private final RoleService roleService;

    /**
     * Create a new Role
     */
    @Allow(Permission.CreateAdministrator)
    public Role createRole(CreateRoleInput input, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        return this.roleService.create(ctx, input);
    }

    /**
     * Update an existing Role
     */
    @Allow(Permission.UpdateAdministrator)
    public Role updateRole(UpdateRoleInput input, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        return this.roleService.update(ctx, input);
    }

    /**
     * Delete an existing Role
     */
    @Allow(Permission.DeleteAdministrator)
    public DeletionResponse deleteRole(Long id, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        return this.roleService.delete(ctx, id);
    }
}
