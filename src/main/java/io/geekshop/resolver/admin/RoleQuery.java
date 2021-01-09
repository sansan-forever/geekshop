/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.resolver.admin;

import io.geekshop.custom.security.Allow;
import io.geekshop.service.RoleService;
import io.geekshop.types.common.Permission;
import io.geekshop.types.role.Role;
import io.geekshop.types.role.RoleList;
import io.geekshop.types.role.RoleListOptions;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class RoleQuery implements GraphQLQueryResolver {

    private final RoleService roleService;

    /**
     * Query
     */
    @Allow(Permission.ReadAdministrator)
    public RoleList roles(RoleListOptions options, DataFetchingEnvironment dfe) {
        return roleService.findAll(options);
    }

    @Allow(Permission.ReadAdministrator)
    public Role role(Long id, DataFetchingEnvironment dfe) {
        return roleService.findOne(id);
    }
}
