/*
 * Copyright (c) 2020 GeekXYZ.
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

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
public class ProductOptionGroupResolver implements GraphQLResolver<ProductOptionGroup> {
    public CompletableFuture<List<ProductOption>> getOptions(ProductOptionGroup optionGroup, DataFetchingEnvironment dfe) {
        final DataLoader<Long, List<ProductOption>> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_PRODUCT_OPTIONS);

        return dataLoader.load(optionGroup.getId());
    }
}
