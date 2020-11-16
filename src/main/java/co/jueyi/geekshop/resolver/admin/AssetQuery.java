/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.types.asset.Asset;
import co.jueyi.geekshop.types.asset.AssetList;
import co.jueyi.geekshop.types.asset.AssetListOptions;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class AssetQuery implements GraphQLQueryResolver {
    /**
     * Get a list of Assets
     */
    public AssetList assets(AssetListOptions options, DataFetchingEnvironment dfe) {
        return null; // TODO
    }

    public Asset asset(Long id, DataFetchingEnvironment dfe) {
        return null; // TODO
    }
}
