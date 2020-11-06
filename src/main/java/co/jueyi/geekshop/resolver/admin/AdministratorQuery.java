package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.types.administrator.Administrator;
import co.jueyi.geekshop.types.administrator.AdministratorList;
import co.jueyi.geekshop.types.administrator.AdministratorListOptions;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
public class AdministratorQuery implements GraphQLQueryResolver {
    public AdministratorList administrators(AdministratorListOptions options) {
        return null; // TODO
    }

    public Administrator administrator(Long id) {
        return null; // TODO
    }
}
