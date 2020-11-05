package co.jueyi.geekshop.custom.graphql;

import graphql.kickstart.execution.context.DefaultGraphQLContext;
import graphql.kickstart.execution.context.GraphQLContext;
import graphql.kickstart.servlet.context.DefaultGraphQLServletContext;
import graphql.kickstart.servlet.context.DefaultGraphQLWebSocketContext;
import graphql.kickstart.servlet.context.GraphQLServletContextBuilder;
import lombok.RequiredArgsConstructor;
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

        // TODO add more data loaders

        return dataLoaderRegistry;
    }
}
