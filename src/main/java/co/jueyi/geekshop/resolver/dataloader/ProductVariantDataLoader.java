/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.dataloader;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.entity.ProductVariantEntity;
import co.jueyi.geekshop.mapper.ProductVariantEntityMapper;
import co.jueyi.geekshop.types.product.ProductVariant;
import org.dataloader.MappedBatchLoader;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * Created on Dec, 2020 by @author bobo
 */
public class ProductVariantDataLoader implements MappedBatchLoader<Long, ProductVariant> {
    private final ProductVariantEntityMapper productVariantEntityMapper;

    public ProductVariantDataLoader(ProductVariantEntityMapper productVariantEntityMapper) {
        this.productVariantEntityMapper = productVariantEntityMapper;
    }

    @Override
    public CompletionStage<Map<Long, ProductVariant>> load(Set<Long> productVariantIds) {
        return CompletableFuture.supplyAsync(() -> {
            List<ProductVariantEntity> productVariantEntities =
                    this.productVariantEntityMapper.selectBatchIds(productVariantIds);
            List<ProductVariant> productVariants = productVariantEntities.stream()
                    .map(productVariantEntity -> BeanMapper.map(productVariantEntity, ProductVariant.class))
                    .collect(Collectors.toList());
            Map<Long, ProductVariant> productVariantMap = productVariants.stream()
                    .collect(Collectors.toMap(ProductVariant::getId, productVariant -> productVariant));
            return productVariantMap;
        });
    }
}
