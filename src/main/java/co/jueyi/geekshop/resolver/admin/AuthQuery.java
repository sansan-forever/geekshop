package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.types.auth.CurrentUser;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
public class AuthQuery implements GraphQLQueryResolver {
    public CurrentUser me() {
        return null; // TODO
    }
}
