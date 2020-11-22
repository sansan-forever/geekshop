/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.dataloader;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.entity.ProductEntity;
import co.jueyi.geekshop.mapper.ProductEntityMapper;
import co.jueyi.geekshop.types.product.Product;
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
public class ProductVariantProductDataLoader implements MappedBatchLoader<Long, Product> {

    private final ProductEntityMapper productEntityMapper;

    @Override
    public CompletionStage<Map<Long, Product>> load(Set<Long> productIds) {
        return CompletableFuture.supplyAsync(() -> {
            List<ProductEntity> productEntities = this.productEntityMapper.selectBatchIds(productIds);
            List<Product> products = productEntities.stream()
                    .map(productEntity -> BeanMapper.map(productEntity, Product.class)).collect(Collectors.toList());
            Map<Long, Product> productMap = products.stream()
                    .collect(Collectors.toMap(Product::getId, product -> product));
            return productMap;
        });
    }
}
