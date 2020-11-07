package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.custom.security.Allow;
import co.jueyi.geekshop.service.RoleService;
import co.jueyi.geekshop.types.common.Permission;
import co.jueyi.geekshop.types.role.Role;
import co.jueyi.geekshop.types.role.RoleList;
import co.jueyi.geekshop.types.role.RoleListOptions;
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
