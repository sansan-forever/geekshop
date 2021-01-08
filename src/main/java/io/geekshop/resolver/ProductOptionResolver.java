/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.resolver;

import io.geekshop.common.Constant;
import io.geekshop.types.product.ProductOption;
import io.geekshop.types.product.ProductOptionGroup;
import graphql.kickstart.execution.context.GraphQLContext;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
public class ProductOptionResolver implements GraphQLResolver<ProductOption> {
    public CompletableFuture<ProductOptionGroup> getGroup(ProductOption productOption, DataFetchingEnvironment dfe) {
        final DataLoader<Long, ProductOptionGroup> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_PRODUCT_OPTION_GROUP);

        return dataLoader.load(productOption.getGroupId());
    }
}
