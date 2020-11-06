package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.types.administrator.Administrator;
import co.jueyi.geekshop.types.administrator.CreateAdministratorInput;
import co.jueyi.geekshop.types.administrator.UpdateAdministratorInput;
import co.jueyi.geekshop.types.common.DeletionResponse;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
public class AdministratorMutation implements GraphQLMutationResolver {
    public Administrator createAdministrator(CreateAdministratorInput input) {
        return null; // TODO
    }

    public Administrator updateAdministrator(UpdateAdministratorInput input) {
        return null; // TODO
    }

    public DeletionResponse deleteAdministrator(Long id) {
        return null; // TODO
    }

    public Administrator assignRoleToAdministrator(Long administratorId, Long roleId) {
        return null; // TODO
    }
}
