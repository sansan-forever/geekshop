/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.resolver.dataloader;

import io.geekshop.common.utils.BeanMapper;
import io.geekshop.entity.AdministratorEntity;
import io.geekshop.mapper.AdministratorEntityMapper;
import io.geekshop.types.administrator.Administrator;
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
public class HistoryEntryAdministratorDataLoader implements MappedBatchLoader<Long, Administrator> {

    private final AdministratorEntityMapper administratorEntityMapper;

    public HistoryEntryAdministratorDataLoader(AdministratorEntityMapper administratorEntityMapper) {
        this.administratorEntityMapper = administratorEntityMapper;
    }

    @Override
    public CompletionStage<Map<Long, Administrator>> load(Set<Long> administratorIds) {
        return CompletableFuture.supplyAsync(() -> {
            List<AdministratorEntity> administratorEntityList =
                    this.administratorEntityMapper.selectBatchIds(administratorIds);
            Map<Long, Administrator> administratorMap = administratorEntityList.stream()
                            .collect(Collectors.toMap(AdministratorEntity::getId,
                                    administratorEntity -> BeanMapper.map(administratorEntity, Administrator.class)));
            return administratorMap;
        });
    }
}
