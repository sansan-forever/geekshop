package co.jueyi.geekshop.custom.graphql;

import co.jueyi.geekshop.common.Constant;
import co.jueyi.geekshop.mapper.*;
import co.jueyi.geekshop.resolver.dataloader.*;
import co.jueyi.geekshop.service.HistoryService;
import co.jueyi.geekshop.types.address.Address;
import co.jueyi.geekshop.types.administrator.Administrator;
import co.jueyi.geekshop.types.customer.CustomerGroup;
import co.jueyi.geekshop.types.history.HistoryEntryList;
import co.jueyi.geekshop.types.role.Role;
import co.jueyi.geekshop.types.user.AuthenticationMethod;
import co.jueyi.geekshop.types.user.User;
import graphql.kickstart.execution.context.DefaultGraphQLContext;
import graphql.kickstart.execution.context.GraphQLContext;
import graphql.kickstart.servlet.context.DefaultGraphQLServletContext;
import graphql.kickstart.servlet.context.DefaultGraphQLWebSocketContext;
import graphql.kickstart.servlet.context.GraphQLServletContextBuilder;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class CustomGraphQLContextBuilder implements GraphQLServletContextBuilder {
    private final UserEntityMapper userEntityMapper;
    private final AdministratorEntityMapper administratorEntityMapper;
    private final CustomerEntityMapper customerEntityMapper;
    private final AddressEntityMapper addressEntityMapper;
    private final CustomerGroupJoinEntityMapper customerGroupJoinEntityMapper;
    private final CustomerGroupEntityMapper customerGroupEntityMapper;
    private final HistoryService historyService;
    private final UserRoleJoinEntityMapper userRoleJoinEntityMapper;
    private final RoleEntityMapper roleEntityMapper;
    private final AuthenticationMethodEntityMapper authenticationMethodEntityMapper;

    @Override
    public GraphQLContext build(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        DefaultGraphQLServletContext defaultGraphQLServletContext =
                DefaultGraphQLServletContext.createServletContext(buildDataLoaderRegistry(), null)
                        .with(httpServletRequest).with(httpServletResponse).build();
        return new CustomGraphQLServletContext(defaultGraphQLServletContext);
    }

    @Override
    public GraphQLContext build(Session session, HandshakeRequest handshakeRequest) {
        return DefaultGraphQLWebSocketContext.createWebSocketContext(buildDataLoaderRegistry(), null)
                .with(session).with(handshakeRequest).build();
    }

    @Override
    public GraphQLContext build() {
        return new DefaultGraphQLContext(buildDataLoaderRegistry(), null);
    }

    private DataLoaderRegistry buildDataLoaderRegistry() {
        DataLoaderRegistry dataLoaderRegistry = new DataLoaderRegistry();

        DataLoader<Long, User> administratorUserDataLoader = DataLoader.newMappedDataLoader(
                new AdministratorUserDataLoader(this.userEntityMapper, this.administratorEntityMapper)
        );
        dataLoaderRegistry.register(Constant.DATA_LOADER_NAME_ADMINISTRATOR_USER, administratorUserDataLoader);

        DataLoader<Long, Administrator> historyEntryAdministratorDataLoader = DataLoader.newMappedDataLoader(
                new HistoryEntryAdministratorDataLoader(this.administratorEntityMapper)
        );
        dataLoaderRegistry.register(
                Constant.DATA_LOADER_NAME_HISTORY_ENTRY_ADMINISTRATOR,
                historyEntryAdministratorDataLoader);

        DataLoader<Long, User> customerUserDataLoader = DataLoader.newMappedDataLoader(
                new CustomerUserDataLoader(this.userEntityMapper, this.customerEntityMapper)
        );
        dataLoaderRegistry.register(Constant.DATA_LOADER_NAME_CUSTOMER_USER, customerUserDataLoader);

        DataLoader<Long, List<Address>> customerAddressesDataLoader = DataLoader.newMappedDataLoader(
                new CustomerAddressesDataLoader(this.addressEntityMapper)
        );
        dataLoaderRegistry.register(Constant.DATA_LOADER_NAME_CUSTOMER_ADDRESSES, customerAddressesDataLoader);

        DataLoader<Long, List<CustomerGroup>> customerGroupDataLoader = DataLoader.newMappedDataLoader(
                new CustomerGroupsDataLoader(this.customerGroupJoinEntityMapper, this.customerGroupEntityMapper)
        );
        dataLoaderRegistry.register(Constant.DATA_LOADER_NAME_CUSTOMER_GROUPS, customerGroupDataLoader);

        DataLoader<Long, HistoryEntryList> customerHistoryDataLoader = DataLoader.newMappedDataLoader(
                new CustomerHistoryDataLoader(this.historyService)
        );
        dataLoaderRegistry.register(Constant.DATA_LOADER_NAME_CUSTOMER_HISTORY, customerHistoryDataLoader);

        DataLoader<Long, List<Role>> userRolesDataLoader = DataLoader.newMappedDataLoader(
                new UserRolesDataLoader(this.userRoleJoinEntityMapper, this.roleEntityMapper)
        );
        dataLoaderRegistry.register(Constant.DATA_LOADER_NAME_USER_ROLES, userRolesDataLoader);

        DataLoader<Long, List<AuthenticationMethod>> userAuthenticationMethodsDataLoader =
                DataLoader.newMappedDataLoader(
                new UserAuthenticationMethodsDataLoader(this.authenticationMethodEntityMapper)
        );
        dataLoaderRegistry.register(
                Constant.DATA_LOADER_NAME_USER_AUTHENTICATION_METHODS, userAuthenticationMethodsDataLoader);

        return dataLoaderRegistry;
    }
}
