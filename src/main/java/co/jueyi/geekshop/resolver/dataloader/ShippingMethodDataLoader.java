/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.dataloader;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.entity.ShippingMethodEntity;
import co.jueyi.geekshop.mapper.ShippingMethodEntityMapper;
import co.jueyi.geekshop.types.shipping.ShippingMethod;
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
public class ShippingMethodDataLoader implements MappedBatchLoader<Long, ShippingMethod> {
    private final ShippingMethodEntityMapper shippingMethodEntityMapper;

    public ShippingMethodDataLoader(ShippingMethodEntityMapper shippingMethodEntityMapper) {
        this.shippingMethodEntityMapper = shippingMethodEntityMapper;
    }

    @Override
    public CompletionStage<Map<Long, ShippingMethod>> load(Set<Long> shippingMethodIds) {
        return CompletableFuture.supplyAsync(() -> {
            List<ShippingMethodEntity> shippingMethodEntities =
                    this.shippingMethodEntityMapper.selectBatchIds(shippingMethodIds);
            List<ShippingMethod> shippingMethods = shippingMethodEntities.stream()
                    .map(shippingMethodEntity -> BeanMapper.map(shippingMethodEntities, ShippingMethod.class))
                    .collect(Collectors.toList());
            Map<Long, ShippingMethod> shippingMethodMap = shippingMethods.stream()
                    .collect(Collectors.toMap(ShippingMethod::getId, m -> m));
            return shippingMethodMap;
        });
    }
}
