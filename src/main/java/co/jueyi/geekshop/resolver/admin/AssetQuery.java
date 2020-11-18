/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.custom.security.Allow;
import co.jueyi.geekshop.entity.AssetEntity;
import co.jueyi.geekshop.service.AssetService;
import co.jueyi.geekshop.types.asset.Asset;
import co.jueyi.geekshop.types.asset.AssetList;
import co.jueyi.geekshop.types.asset.AssetListOptions;
import co.jueyi.geekshop.types.common.Permission;
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
