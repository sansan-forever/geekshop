/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver;

import co.jueyi.geekshop.common.ApiType;
import co.jueyi.geekshop.common.Constant;
import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.service.StockMovementService;
import co.jueyi.geekshop.types.asset.Asset;
import co.jueyi.geekshop.types.facet.FacetValue;
import co.jueyi.geekshop.types.product.Product;
import co.jueyi.geekshop.types.product.ProductOption;
import co.jueyi.geekshop.types.product.ProductVariant;
import co.jueyi.geekshop.types.product.StockMovementListOptions;
import co.jueyi.geekshop.types.stock.StockMovementList;
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
public class ProductVariantResolver implements GraphQLResolver<ProductVariant> {

    private final StockMovementService stockMovementService;

    public CompletableFuture<Product> getProduct(ProductVariant productVariant, DataFetchingEnvironment dfe) {
        if (productVariant.getProduct() == null) {
            CompletableFuture<Product> completableFuture = new CompletableFuture<>();
            completableFuture.complete(null);
            return completableFuture;
        }

        final DataLoader<Long, Product> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_PRODUCT_VARIANT_PRODUCT);

        return dataLoader.load(productVariant.getProductId());
    }

    public CompletableFuture<Asset> getFeaturedAsset(ProductVariant productVariant, DataFetchingEnvironment dfe) {
        if (productVariant.getFeaturedAssetId() == null) {
            CompletableFuture<Asset> completableFuture = new CompletableFuture<>();
            completableFuture.complete(null);
            return completableFuture;
        }

        final DataLoader<Long, Asset> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_PRODUCT_VARIANT_FEATURED_ASSET);

        return dataLoader.load(productVariant.getFeaturedAssetId());
    }

    public CompletableFuture<List<Asset>> getAssets(ProductVariant productVariant, DataFetchingEnvironment dfe) {
        final DataLoader<Long, List<Asset>> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_PRODUCT_VARIANT_ASSETS);

        return dataLoader.load(productVariant.getId());
    }

    public CompletableFuture<List<ProductOption>> getOptions(ProductVariant productVariant,
                                                             DataFetchingEnvironment dfe) {
        final DataLoader<Long, List<ProductOption>> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_PRODUCT_VARIANT_OPTIONS);

        return dataLoader.load(productVariant.getId());
    }

    public CompletableFuture<List<FacetValue>> getFacetValues(ProductVariant productVariant, DataFetchingEnvironment dfe) {
        final DataLoader<Long, List<FacetValue>> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_PRODUCT_VARIANT_FACET_VALUES);

        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);

        return dataLoader.load(productVariant.getId(), ctx);
    }

    public StockMovementList getStockMovements(
            ProductVariant productVariant, StockMovementListOptions options, DataFetchingEnvironment dfe) {

        // 只对Admin可见
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        if (ApiType.SHOP.equals(ctx.getApiType())) {
            return null;
        }

        return stockMovementService.getStockMovementsByProductVaraintId(productVariant.getId(), options);
    }
}
