/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.dataloader;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.entity.CollectionEntity;
import co.jueyi.geekshop.mapper.CollectionEntityMapper;
import co.jueyi.geekshop.types.collection.Collection;
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
