/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.resolver.dataloader;

import io.geekshop.common.utils.BeanMapper;
import io.geekshop.entity.AssetEntity;
import io.geekshop.entity.ProductAssetJoinEntity;
import io.geekshop.mapper.AssetEntityMapper;
import io.geekshop.mapper.ProductAssetJoinEntityMapper;
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
@RequiredArgsConstructor
@SuppressWarnings("Duplicates")
public class ProductAssetsDataLoader implements MappedBatchLoader<Long, List<Asset>> {
    private final ProductAssetJoinEntityMapper productAssetJoinEntityMapper;
    private final AssetEntityMapper assetEntityMapper;

    @Override
    public CompletionStage<Map<Long, List<Asset>>> load(Set<Long> productIds) {
        return CompletableFuture.supplyAsync(() -> {
            Map<Long, List<Asset>> productAssetsMap = new HashMap<>();
            productIds.forEach(id -> productAssetsMap.put(id, new ArrayList<>()));

            QueryWrapper<ProductAssetJoinEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(ProductAssetJoinEntity::getProductId, productIds);
            List<ProductAssetJoinEntity> joinEntities =
                    productAssetJoinEntityMapper.selectList(queryWrapper);
            if (CollectionUtils.isEmpty(joinEntities)) return productAssetsMap;

            Set<Long> assetIds = joinEntities.stream()
                    .map(ProductAssetJoinEntity::getAssetId).collect(Collectors.toSet());
            List<AssetEntity> assetEntityList = assetEntityMapper.selectBatchIds(assetIds);
            if (CollectionUtils.isEmpty(assetEntityList)) return productAssetsMap;

            Map<Long, AssetEntity> assetEntityMap = assetEntityList.stream()
                    .collect(Collectors.toMap(AssetEntity::getId, assetEntity -> assetEntity));

            joinEntities.forEach(productAssetJoinEntity -> {
                Long productId = productAssetJoinEntity.getProductId();
                Long assetId = productAssetJoinEntity.getAssetId();
                List<Asset> assetList = productAssetsMap.get(productId);
                AssetEntity assetEntity = assetEntityMap.get(assetId);
                Asset asset = BeanMapper.patch(assetEntity, Asset.class);
                assetList.add(asset);
            });

            return productAssetsMap;
        });
    }
}
