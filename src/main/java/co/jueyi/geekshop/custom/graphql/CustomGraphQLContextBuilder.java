package co.jueyi.geekshop.custom.graphql;

import co.jueyi.geekshop.common.Constant;
import co.jueyi.geekshop.mapper.AdministratorEntityMapper;
import co.jueyi.geekshop.mapper.UserEntityMapper;
import co.jueyi.geekshop.resolver.admin.dataloader.AdministratorUserDataLoader;
import co.jueyi.geekshop.resolver.admin.dataloader.HistoryEntryAdministratorDataLoader;
import co.jueyi.geekshop.types.administrator.Administrator;
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

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class CustomGraphQLContextBuilder implements GraphQLServletContextBuilder {
    private final UserEntityMapper userEntityMapper;
    private final AdministratorEntityMapper administratorEntityMapper;

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

        return dataLoaderRegistry;
    }
}
