package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.types.auth.AuthenticationInput;
import co.jueyi.geekshop.types.auth.LoginResult;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
public class AuthMutation implements GraphQLMutationResolver {
    /**
     * Authenticates the user using the native authentication strategy. This mutation is a alias for
     * `authenticate({ native: { ... }})`
     */
    public LoginResult login(String username, String password, Boolean rememberMe) {
        return null; // TODO
    }

    /**
     * Authenticates the user using a named authentication strategy
     */
    public LoginResult authenticate(AuthenticationInput input, Boolean rememberMe) {
        return null; // TODO
    }

    public Boolean logout() {
        return null; // TODO
    }


}
