package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.types.common.DeletionResponse;
import co.jueyi.geekshop.types.role.*;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
public class RoleMutation implements GraphQLMutationResolver {
    /**
     * Mutation
     */

    /**
     * Create a new Role
     */
    public Role createRole(CreateRoleInput input) {
        return null; // TODO
    }

    /**
     * Update an existing Role
     */
    public Role updateRole(UpdateRoleInput input) {
        return null; // TODO
    }

    /**
     * Delete an existing Role
     */
    public DeletionResponse deleteRole(Long id) {
        return null; // TODO
    }
}
