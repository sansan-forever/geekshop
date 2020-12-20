/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver;

import co.jueyi.geekshop.common.Constant;
import co.jueyi.geekshop.types.order.OrderItem;
import co.jueyi.geekshop.types.payment.Refund;
import graphql.kickstart.execution.context.GraphQLContext;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class RefundResolver implements GraphQLResolver<Refund> {
    public CompletableFuture<List<OrderItem>> getOrderItems(Refund refund, DataFetchingEnvironment dfe) {
        final DataLoader<Long, List<OrderItem>> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_REFUND_ORDER_ITEMS);

        return dataLoader.load(refund.getId());
    }
}
