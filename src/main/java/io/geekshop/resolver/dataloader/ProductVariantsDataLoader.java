/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.resolver.dataloader;

import io.geekshop.common.ApiType;
import io.geekshop.common.RequestContext;
import io.geekshop.common.utils.BeanMapper;
import io.geekshop.entity.ProductVariantEntity;
import io.geekshop.mapper.ProductVariantEntityMapper;
import io.geekshop.types.product.ProductVariant;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoaderEnvironment;
import org.dataloader.MappedBatchLoaderWithContext;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * Created on Nov, 2020 by @author bobo
 */
@RequiredArgsConstructor
public class ProductVariantsDataLoader implements MappedBatchLoaderWithContext<Long, List<ProductVariant>> {
    private final ProductVariantEntityMapper productVariantEntityMapper;

    @Override
    public CompletionStage<Map<Long, List<ProductVariant>>> load(
            Set<Long> productIds, BatchLoaderEnvironment environment) {
        return CompletableFuture.supplyAsync(() -> {
            QueryWrapper<ProductVariantEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(ProductVariantEntity::getProductId, productIds)
                    .isNull(ProductVariantEntity::getDeletedAt) // 没有软删除的
                    .orderByAsc(ProductVariantEntity::getId);
            List<ProductVariantEntity> variantEntities = productVariantEntityMapper.selectList(queryWrapper);

            if (variantEntities.size() == 0) {
                Map<Long, List<ProductVariant>> emptyMap = new HashMap<>();
                productIds.forEach(id -> emptyMap.put(id, new ArrayList<>()));
                return emptyMap;
            }

            Map<Object, Object> optionsMap = environment.getKeyContexts();
            RequestContext ctx = (RequestContext) optionsMap.get(productIds.iterator().next());
            boolean isAdmin = ApiType.ADMIN.equals(ctx.getApiType());
            List<ProductVariantEntity> enabledVariantEntities = variantEntities.stream()
                    .filter(v -> isAdmin ? true : v.isEnabled())
                    .collect(Collectors.toList());

            Map<Long, List<ProductVariant>> groupByProductIdMap =
                    enabledVariantEntities.stream().collect(groupingBy(ProductVariantEntity::getProductId,
                            Collectors.mapping(productVariantEntity ->
                                            BeanMapper.map(productVariantEntity, ProductVariant.class),
                                    Collectors.toList())));
            return groupByProductIdMap;
        });
    }
}
