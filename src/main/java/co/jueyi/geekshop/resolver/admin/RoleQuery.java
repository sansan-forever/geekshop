package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.types.role.Role;
import co.jueyi.geekshop.types.role.RoleList;
import co.jueyi.geekshop.types.role.RoleListOptions;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
public class RoleQuery implements GraphQLQueryResolver {
    /**
     * Query
     */
    public RoleList roles(RoleListOptions options) {
        return null; // TODO
    }

    public Role role(Long id) {
        return null; // TODO
    }
}
