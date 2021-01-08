/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.resolver.dataloader;

import io.geekshop.common.utils.BeanMapper;
import io.geekshop.entity.AssetEntity;
import io.geekshop.entity.CollectionAssetJoinEntity;
import io.geekshop.mapper.AssetEntityMapper;
import io.geekshop.mapper.CollectionAssetJoinEntityMapper;
import io.geekshop.types.asset.Asset;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.dataloader.MappedBatchLoader;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * Created on Nov, 2020 by @author bobo
 */
@SuppressWarnings("Duplicates")
@RequiredArgsConstructor
public class CollectionAssetsDataLoader implements MappedBatchLoader<Long, List<Asset>> {

    private final CollectionAssetJoinEntityMapper collectionAssetJoinEntityMapper;
    private final AssetEntityMapper assetEntityMapper;

    @Override
    public CompletionStage<Map<Long, List<Asset>>> load(Set<Long> collectionIds) {
        return CompletableFuture.supplyAsync(() -> {
            Map<Long, List<Asset>> collectionAssetsMap = new HashMap<>();
            collectionIds.forEach(id -> collectionAssetsMap.put(id, new ArrayList<>()));

            QueryWrapper<CollectionAssetJoinEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(CollectionAssetJoinEntity::getCollectionId, collectionIds);
            List<CollectionAssetJoinEntity> collectionAssetJoinEntities =
                    collectionAssetJoinEntityMapper.selectList(queryWrapper);
            if (CollectionUtils.isEmpty(collectionAssetJoinEntities)) return collectionAssetsMap;

            Set<Long> assetIds = collectionAssetJoinEntities.stream()
                    .map(CollectionAssetJoinEntity::getAssetId).collect(Collectors.toSet());
            List<AssetEntity> assetEntityList = assetEntityMapper.selectBatchIds(assetIds);
            if (CollectionUtils.isEmpty(assetEntityList)) return collectionAssetsMap;

            Map<Long, AssetEntity> assetEntityMap = assetEntityList.stream()
                    .collect(Collectors.toMap(AssetEntity::getId, assetEntity -> assetEntity));

            collectionAssetJoinEntities.forEach(joinEntity -> {
                Long collectionId = joinEntity.getCollectionId();
                Long assetId = joinEntity.getAssetId();
                List<Asset> assetList = collectionAssetsMap.get(collectionId);
                AssetEntity assetEntity = assetEntityMap.get(assetId);
                Asset asset = BeanMapper.patch(assetEntity, Asset.class);
                assetList.add(asset);
            });

            return collectionAssetsMap;
        });
    }
}
