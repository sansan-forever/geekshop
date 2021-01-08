/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.resolver.dataloader;

import io.geekshop.common.utils.BeanMapper;
import io.geekshop.entity.AssetEntity;
import io.geekshop.mapper.AssetEntityMapper;
import io.geekshop.types.asset.Asset;
import org.dataloader.MappedBatchLoader;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * Created on Nov, 2020 by @author bobo
 */

public class FeaturedAssetDataLoader implements MappedBatchLoader<Long, Asset> {
    private final AssetEntityMapper assetEntityMapper;

    public FeaturedAssetDataLoader(AssetEntityMapper assetEntityMapper) {
        this.assetEntityMapper = assetEntityMapper;
    }

    @Override
    public CompletionStage<Map<Long, Asset>> load(Set<Long> assetIds) {
        return CompletableFuture.supplyAsync(() -> {
            List<AssetEntity> assetEntityList = this.assetEntityMapper.selectBatchIds(assetIds);
            List<Asset> assetList = assetEntityList.stream()
                    .map(assetEntity -> BeanMapper.map(assetEntity, Asset.class)).collect(Collectors.toList());
            Map<Long, Asset> featuredAssetMap = assetList.stream()
                    .collect(Collectors.toMap(Asset::getId, asset -> asset));
            return featuredAssetMap;
        });
    }
}
