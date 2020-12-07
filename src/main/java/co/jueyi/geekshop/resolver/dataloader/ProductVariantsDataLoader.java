/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.dataloader;

import co.jueyi.geekshop.common.ApiType;
import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.entity.ProductVariantEntity;
import co.jueyi.geekshop.mapper.ProductVariantEntityMapper;
import co.jueyi.geekshop.types.product.ProductVariant;
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
