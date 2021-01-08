/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.resolver;

import io.geekshop.common.ApiType;
import io.geekshop.common.Constant;
import io.geekshop.common.RequestContext;
import io.geekshop.service.CollectionService;
import io.geekshop.service.ProductVariantService;
import io.geekshop.types.asset.Asset;
import io.geekshop.types.collection.Collection;
import io.geekshop.types.collection.CollectionBreadcrumb;
import io.geekshop.types.common.BooleanOperators;
import io.geekshop.types.product.ProductVariantFilterParameter;
import io.geekshop.types.product.ProductVariantList;
import io.geekshop.types.product.ProductVariantListOptions;
import graphql.kickstart.execution.context.GraphQLContext;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class CollectionResolver implements GraphQLResolver<Collection> {

    private final CollectionService collectionService;
    private final ProductVariantService productVariantService;

    public CompletableFuture<Asset> getFeaturedAsset(Collection collection, DataFetchingEnvironment dfe) {
        if (collection.getFeaturedAssetId() == null) {
            CompletableFuture<Asset> completableFuture = new CompletableFuture<>();
            completableFuture.complete(null);
            return completableFuture;
        }

        final DataLoader<Long, Asset> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_COLLECTION_FEATURED_ASSET);

        return dataLoader.load(collection.getFeaturedAssetId());
    }

    public CompletableFuture<List<Asset>> getAssets(Collection collection, DataFetchingEnvironment dfe) {
        final DataLoader<Long, List<Asset>> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_COLLECTION_ASSETS);

        return dataLoader.load(collection.getId());
    }

    public CompletableFuture<Collection> getParent(Collection collection, DataFetchingEnvironment dfe) {
        if (collection.getParentId() == null) {
            CompletableFuture<Collection> completableFuture = new CompletableFuture<>();
            completableFuture.complete(null);
            return completableFuture;
        }

        final DataLoader<Long, Collection> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_COLLECTION_PARENT);

        return dataLoader.load(collection.getParentId());
    }

    public CompletableFuture<List<Collection>> getChildren(
            Collection collection, DataFetchingEnvironment dfe) {
        final DataLoader<Long, List<Collection>> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_COLLECTION_CHILDREN);

        return dataLoader.load(collection.getId());
    }

    public List<CollectionBreadcrumb> getBreadcrumbs(Collection collection, DataFetchingEnvironment dfe) {
        return collectionService.getBreadcrumbs(collection.getId());
    }

    public ProductVariantList getProductVariants(
            Collection collection, ProductVariantListOptions options, DataFetchingEnvironment dfe) {

        // enabled = true才对SHOP可见
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        if (ApiType.SHOP.equals(ctx.getApiType())) {
            if (options == null) {
                options = new ProductVariantListOptions();
            }
            if (options.getFilter() == null) {
                options.setFilter(new ProductVariantFilterParameter());
            }
            ProductVariantFilterParameter filter = options.getFilter();
            BooleanOperators booleanOperators = new BooleanOperators();
            booleanOperators.setEq(true);
            filter.setEnabled(booleanOperators);
        }

        return this.productVariantService.getVariantsByCollectionId(collection.getId(), options);
    }
}
