/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.resolver.admin;

import io.geekshop.common.RequestContext;
import io.geekshop.custom.security.Allow;
import io.geekshop.service.RoleService;
import io.geekshop.types.common.DeletionResponse;
import io.geekshop.types.common.Permission;
import io.geekshop.types.role.*;
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
