/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver;

import co.jueyi.geekshop.common.Constant;
import co.jueyi.geekshop.types.facet.Facet;
import co.jueyi.geekshop.types.facet.FacetValue;
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
public class FacetValueResolver implements GraphQLResolver<FacetValue> {
    public CompletableFuture<Facet> getFacet(FacetValue facetValue, DataFetchingEnvironment dfe) {
        final DataLoader<Long, Facet> dataLoader = ((GraphQLContext) dfe.getContext())
                .getDataLoaderRegistry().get()
                .getDataLoader(Constant.DATA_LOADER_NAME_FACET_VALUE_FACET);

        return dataLoader.load(facetValue.getFacetId());
    }
}
