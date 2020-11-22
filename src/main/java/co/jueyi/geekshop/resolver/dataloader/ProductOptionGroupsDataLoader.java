/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.dataloader;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.entity.ProductOptionGroupEntity;
import co.jueyi.geekshop.entity.ProductOptionGroupJoinEntity;
import co.jueyi.geekshop.mapper.ProductOptionGroupEntityMapper;
import co.jueyi.geekshop.mapper.ProductOptionGroupJoinEntityMapper;
import co.jueyi.geekshop.types.product.ProductOptionGroup;
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
public class ProductOptionGroupsDataLoader implements MappedBatchLoader<Long, List<ProductOptionGroup>> {
    private final ProductOptionGroupJoinEntityMapper productOptionGroupJoinEntityMapper;
    private final ProductOptionGroupEntityMapper productOptionGroupEntityMapper;

    @Override
    public CompletionStage<Map<Long, List<ProductOptionGroup>>> load(Set<Long> productIds) {
        return CompletableFuture.supplyAsync(() -> {
            Map<Long, List<ProductOptionGroup>> optionGroupsMap = new HashMap<>();
            productIds.forEach(id -> optionGroupsMap.put(id, new ArrayList<>()));

            QueryWrapper<ProductOptionGroupJoinEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(ProductOptionGroupJoinEntity::getProductId, productIds);
            List<ProductOptionGroupJoinEntity> joinEntities =
                    productOptionGroupJoinEntityMapper.selectList(queryWrapper);
            if (CollectionUtils.isEmpty(joinEntities)) return optionGroupsMap;

            Set<Long> optionGroupIds = joinEntities.stream()
                    .map(ProductOptionGroupJoinEntity::getOptionGroupId).collect(Collectors.toSet());
            List<ProductOptionGroupEntity> productOptionGroupEntities =
                    productOptionGroupEntityMapper.selectBatchIds(optionGroupIds);
            if (CollectionUtils.isEmpty(productOptionGroupEntities)) return optionGroupsMap;

            Map<Long, ProductOptionGroupEntity> optionGroupEntityMap = productOptionGroupEntities.stream()
                    .collect(Collectors.toMap(ProductOptionGroupEntity::getId,
                            optionGroupEntity -> optionGroupEntity));

            joinEntities.forEach(optionGroupJoinEntity -> {
                Long productId = optionGroupJoinEntity.getProductId();
                Long optionGroupId = optionGroupJoinEntity.getOptionGroupId();
                List<ProductOptionGroup> optionGroups = optionGroupsMap.get(productId);
                ProductOptionGroupEntity optionGroupEntity = optionGroupEntityMap.get(optionGroupId);
                ProductOptionGroup optionGroup = BeanMapper.patch(optionGroupEntity, ProductOptionGroup.class);
                optionGroups.add(optionGroup);
            });

            return optionGroupsMap;
        });
    }
}
