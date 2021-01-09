/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.resolver;

import io.geekshop.common.Constant;
import io.geekshop.types.asset.Asset;
import io.geekshop.types.order.Order;
import io.geekshop.types.order.OrderItem;
import io.geekshop.types.order.OrderLine;
import io.geekshop.types.product.ProductVariant;
import graphql.kickstart.execution.context.GraphQLContext;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Component
public class OrderLineResolver implements GraphQLResolver<OrderLine> {
    public CompletableFuture<ProductVariant> getProductVariant(OrderLine orderLine, DataFetchingEnvironment dfe) {
        final DataLoader<Long, ProductVariant> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_ORDER_LINE_PRODUCT_VARIANT);

        return dataLoader.load(orderLine.getProductVariantId());
    }

    public CompletableFuture<Asset> getFeaturedAsset(OrderLine orderLine, DataFetchingEnvironment dfe) {
        if (orderLine.getFeaturedAssetId() == null) {
            CompletableFuture<Asset> completableFuture = new CompletableFuture<>();
            completableFuture.complete(null);
            return completableFuture;
        }

        final DataLoader<Long, Asset> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_ORDER_LINE_FEATURED_ASSET);

        return dataLoader.load(orderLine.getFeaturedAssetId());
    }

    public CompletableFuture<List<OrderItem>> getItems(OrderLine orderLine, DataFetchingEnvironment dfe) {
        final DataLoader<Long, List<OrderItem>> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_ORDER_LINE_ITEMS);

        return dataLoader.load(orderLine.getId());
    }

    public CompletableFuture<Order> getOrder(OrderLine orderLine, DataFetchingEnvironment dfe) {
        final DataLoader<Long, Order> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_ORDER_LINE_ORDER);

        return dataLoader.load(orderLine.getOrderId());
    }
}
