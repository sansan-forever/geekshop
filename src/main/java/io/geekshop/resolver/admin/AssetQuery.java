/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.resolver.admin;

import io.geekshop.common.utils.BeanMapper;
import io.geekshop.custom.security.Allow;
import io.geekshop.entity.AssetEntity;
import io.geekshop.service.AssetService;
import io.geekshop.types.asset.Asset;
import io.geekshop.types.asset.AssetList;
import io.geekshop.types.asset.AssetListOptions;
import io.geekshop.types.common.Permission;
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
    private final AssetService assetService;

    /**
     * Get a list of Assets
     */
    @Allow(Permission.ReadCatalog)
    public AssetList assets(AssetListOptions options, DataFetchingEnvironment dfe) {
        return this.assetService.findAll(options);
    }

    public Asset asset(Long id, DataFetchingEnvironment dfe) {
        AssetEntity assetEntity = this.assetService.findOne(id);
        if (assetEntity == null) return null;
        return BeanMapper.map(assetEntity, Asset.class);
    }
}
