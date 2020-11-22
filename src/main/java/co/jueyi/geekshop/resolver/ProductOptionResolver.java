/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver;

import co.jueyi.geekshop.common.Constant;
import co.jueyi.geekshop.types.product.ProductOption;
import co.jueyi.geekshop.types.product.ProductOptionGroup;
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
