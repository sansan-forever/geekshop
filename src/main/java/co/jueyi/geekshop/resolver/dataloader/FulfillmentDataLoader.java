/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.dataloader;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.entity.FulfillmentEntity;
import co.jueyi.geekshop.mapper.FulfillmentEntityMapper;
import co.jueyi.geekshop.types.order.Fulfillment;
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
public class FulfillmentDataLoader implements MappedBatchLoader<Long, Fulfillment> {

    private final FulfillmentEntityMapper fulfillmentEntityMapper;

    public FulfillmentDataLoader(FulfillmentEntityMapper fulfillmentEntityMapper) {
        this.fulfillmentEntityMapper = fulfillmentEntityMapper;
    }

    @Override
    public CompletionStage<Map<Long, Fulfillment>> load(Set<Long> fulfillmentIds) {
        return CompletableFuture.supplyAsync(() -> {
            List<FulfillmentEntity> fulfillmentEntities =
                    this.fulfillmentEntityMapper.selectBatchIds(fulfillmentIds);
            List<Fulfillment> fulfillments = fulfillmentEntities.stream()
                    .map(fulfillmentEntity -> BeanMapper.map(fulfillmentEntity, Fulfillment.class))
                    .collect(Collectors.toList());
            Map<Long, Fulfillment> fulfillmentMap = fulfillments.stream()
                    .collect(Collectors.toMap(Fulfillment::getId, f -> f));
            return fulfillmentMap;
        });
    }
}
