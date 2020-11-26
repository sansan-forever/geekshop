/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.eventbus;

import co.jueyi.geekshop.common.ConfigArgValues;
import co.jueyi.geekshop.config.collection.CollectionFilter;
import co.jueyi.geekshop.entity.CollectionEntity;
import co.jueyi.geekshop.entity.ProductVariantCollectionJoinEntity;
import co.jueyi.geekshop.entity.ProductVariantEntity;
import co.jueyi.geekshop.eventbus.events.ApplyCollectionFilterEvent;
import co.jueyi.geekshop.eventbus.events.CollectionModificationEvent;
import co.jueyi.geekshop.mapper.CollectionEntityMapper;
import co.jueyi.geekshop.mapper.ProductVariantCollectionJoinEntityMapper;
import co.jueyi.geekshop.mapper.ProductVariantEntityMapper;
import co.jueyi.geekshop.service.CollectionService;
import co.jueyi.geekshop.service.ConfigService;
import co.jueyi.geekshop.types.common.ConfigurableOperation;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Updates collections on the background event handler because running the CollectionFilters
 * is computationally expensive.
 *
 * Created on Nov, 2020 by @author bobo
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ApplyCollectionFilterEventSubscriber {
    private final ConfigService configService;
    private final CollectionService collectionService;
    private final CollectionEntityMapper collectionEntityMapper;
    private final ProductVariantEntityMapper productVariantEntityMapper;
    private final ProductVariantCollectionJoinEntityMapper productVariantCollectionJoinEntityMapper;

    private final EventBus eventBus;

    @PostConstruct
    void init() {
        eventBus.register(this);
    }

    @Subscribe
    public void onEvent(ApplyCollectionFilterEvent event) {
        List<Long> collectionIds = event.getCollectionIds();
        log.info("Processing " + collectionIds.size() + " Collections");
        List<CollectionEntity> collectionEntities = this.collectionEntityMapper.selectBatchIds(collectionIds);
        for(CollectionEntity collectionEntity : collectionEntities) {
            Set<Long> affectedVariantIds = this.applyCollectionFiltersInternal(collectionEntity);
            if (!CollectionUtils.isEmpty(affectedVariantIds)) {
                CollectionModificationEvent collectionModificationEvent =
                        new CollectionModificationEvent(event.getCtx(), collectionEntity, affectedVariantIds);
                this.eventBus.post(collectionModificationEvent);
            }
        }
    }

    /**
     * Applies the CollectionFilters and returns an array of all affected ProductVariant ids.
     */
    private Set<Long> applyCollectionFiltersInternal(CollectionEntity collectionEntity) {
        List<ConfigurableOperation> filters = new ArrayList<>();
        List<CollectionEntity> ancestors = this.collectionService.getAncestors(collectionEntity.getId());
        for (CollectionEntity ancestor : ancestors) {
            filters.addAll(ancestor.getFilters());
        }

        Set<Long> preIds = this.collectionService.getCollectionProductVariantIds(collectionEntity.getId());
        filters.addAll(collectionEntity.getFilters());
        List<ProductVariantEntity> productVariantEntities = this.getFilteredProductVariants(filters);
        Set<Long> postIds = productVariantEntities.stream()
                .map(ProductVariantEntity::getId).collect(Collectors.toSet());

        // 清除现有关联关系
        QueryWrapper<ProductVariantCollectionJoinEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ProductVariantCollectionJoinEntity::getCollectionId, collectionEntity.getId());
        this.productVariantCollectionJoinEntityMapper.delete(queryWrapper);
        // 添加新的关联关系
        for(Long productVariantId : postIds) {
            ProductVariantCollectionJoinEntity joinEntity = new ProductVariantCollectionJoinEntity();
            joinEntity.setCollectionId(collectionEntity.getId());
            joinEntity.setProductVariantId(productVariantId);
            this.productVariantCollectionJoinEntityMapper.insert(joinEntity);
        }

        Set<Long> difference = Sets.symmetricDifference(preIds, postIds);
        return difference;
    }

    /**
     * Applies the CollectionFilters and returns an array of ProductVariant entities which match.
     */
    private List<ProductVariantEntity> getFilteredProductVariants(List<ConfigurableOperation> filters) {
        if (CollectionUtils.isEmpty(filters)) return new ArrayList<>();

        List<CollectionFilter> collectionFilters = this.configService.getCatalogConfig().getCollectionFilters();

        QueryWrapper<ProductVariantEntity> queryWrapper = new QueryWrapper<>();

        for(CollectionFilter filterType : collectionFilters) {
            List<ConfigurableOperation> filtersOfType = filters.stream()
                    .filter(f -> Objects.equals(f.getCode(), filterType.getCode()))
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(filtersOfType)) {
                for(ConfigurableOperation filter : filtersOfType) {
                    queryWrapper = filterType.apply(new ConfigArgValues(filter.getArgs()), queryWrapper);
                }
            }
        }

        return this.productVariantEntityMapper.selectList(queryWrapper);
    }
}
