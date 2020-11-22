/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver;

import co.jueyi.geekshop.common.Constant;
import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.types.asset.Asset;
import co.jueyi.geekshop.types.facet.FacetValue;
import co.jueyi.geekshop.types.product.Product;
import co.jueyi.geekshop.types.product.ProductOptionGroup;
import co.jueyi.geekshop.types.product.ProductVariant;
import graphql.kickstart.execution.context.GraphQLContext;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
public class ProductResolver implements GraphQLResolver<Product> {
    public CompletableFuture<Asset> getFeaturedAsset(Product product, DataFetchingEnvironment dfe) {
        if (product.getFeaturedAssetId() == null) {
            CompletableFuture<Asset> completableFuture = new CompletableFuture<>();
            completableFuture.complete(null);
            return completableFuture;
        }

        final DataLoader<Long, Asset> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_PRODUCT_FEATURED_ASSET);

        return dataLoader.load(product.getFeaturedAssetId());
    }

    public CompletableFuture<List<Asset>> getAssets(Product product, DataFetchingEnvironment dfe) {
        final DataLoader<Long, List<Asset>> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_PRODUCT_ASSETS);

        return dataLoader.load(product.getId());
    }

    public CompletableFuture<List<ProductVariant>> getVariants(Product product, DataFetchingEnvironment dfe) {
        final DataLoader<Long, List<ProductVariant>> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_PRODUCT_VARIANTS);

        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);

        return dataLoader.load(product.getId(), ctx);
    }

    public CompletableFuture<List<ProductOptionGroup>> getOptionGroups(Product product, DataFetchingEnvironment dfe) {
        final DataLoader<Long, List<ProductOptionGroup>> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_PRODUCT_OPTION_GROUPS);

        return dataLoader.load(product.getId());
    }

    public CompletableFuture<List<FacetValue>> getFacetValues(Product product, DataFetchingEnvironment dfe) {
        final DataLoader<Long, List<FacetValue>> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_PRODUCT_FACET_VALUES);

        return dataLoader.load(product.getId());
    }

    // TODO getCollections *shop only*
}
