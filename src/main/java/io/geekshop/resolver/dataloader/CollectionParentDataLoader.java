/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.resolver.dataloader;

import io.geekshop.common.utils.BeanMapper;
import io.geekshop.entity.CollectionEntity;
import io.geekshop.mapper.CollectionEntityMapper;
import io.geekshop.types.collection.Collection;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class CollectionParentDataLoader implements MappedBatchLoader<Long, Collection> {
    private final CollectionEntityMapper collectionEntityMapper;

    @Override
    public CompletionStage<Map<Long, Collection>> load(Set<Long> parentIds) {
        return CompletableFuture.supplyAsync(() -> {
            List<CollectionEntity> collectionEntities =
                    this.collectionEntityMapper.selectBatchIds(parentIds);
            List<Collection> collections = collectionEntities.stream()
                    .map(collectionEntity -> BeanMapper.map(collectionEntity, Collection.class))
                    .collect(Collectors.toList());
            Map<Long, Collection> collectionMap =
                    collections.stream().collect(Collectors.toMap(Collection::getId, c -> c));
            return collectionMap;
        });
    }
}
