package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.custom.security.Allow;
import co.jueyi.geekshop.resolver.base.BaseAuthQuery;
import co.jueyi.geekshop.service.AdministratorService;
import co.jueyi.geekshop.service.UserService;
import co.jueyi.geekshop.types.auth.CurrentUser;
import co.jueyi.geekshop.types.common.Permission;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
public class AuthQuery extends BaseAuthQuery implements GraphQLQueryResolver {
    public AuthQuery(AdministratorService administratorService, UserService userService) {
        super(administratorService, userService);
    }

    @Allow({Permission.Authenticated, Permission.Owner})
    public CurrentUser me(DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        return super.me(ctx);
    }
}
