/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.dataloader;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.entity.ProductOptionEntity;
import co.jueyi.geekshop.mapper.ProductOptionEntityMapper;
import co.jueyi.geekshop.types.product.ProductOption;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.dataloader.MappedBatchLoader;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * Created on Nov, 2020 by @author bobo
 */
public class ProductOptionsDataLoader implements MappedBatchLoader<Long, List<ProductOption>> {

    private final ProductOptionEntityMapper productOptionEntityMapper;

    public ProductOptionsDataLoader(ProductOptionEntityMapper productOptionEntityMapper) {
        this.productOptionEntityMapper = productOptionEntityMapper;
    }

    @Override
    public CompletionStage<Map<Long, List<ProductOption>>> load(Set<Long> groupIds) {
        return CompletableFuture.supplyAsync(() -> {
            QueryWrapper<ProductOptionEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(ProductOptionEntity::getGroupId, groupIds);
            List<ProductOptionEntity> productOptionEntities = this.productOptionEntityMapper.selectList(queryWrapper);

            if (productOptionEntities.size() == 0) {
                Map<Long, List<ProductOption>> emptyMap = new HashMap<>();
                groupIds.forEach(id -> emptyMap.put(id, new ArrayList<>()));
                return emptyMap;
            }

            Map<Long, List<ProductOption>> groupByGroupId = productOptionEntities.stream()
                    .collect(Collectors.groupingBy(ProductOptionEntity::getGroupId,
                            Collectors.mapping(optionEntity -> BeanMapper.map(optionEntity, ProductOption.class),
                                    Collectors.toList()
                            )));

            return groupByGroupId;
        });
    }
}
