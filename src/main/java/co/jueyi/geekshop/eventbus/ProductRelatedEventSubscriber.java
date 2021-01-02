/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.eventbus;

import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.entity.CollectionEntity;
import co.jueyi.geekshop.eventbus.events.ApplyCollectionFilterEvent;
import co.jueyi.geekshop.eventbus.events.ProductEvent;
import co.jueyi.geekshop.eventbus.events.ProductVariantEvent;
import co.jueyi.geekshop.mapper.CollectionEntityMapper;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ProductRelatedEventSubscriber {

    private final CollectionEntityMapper collectionEntityMapper;
    private final EventBus eventBus;

    @PostConstruct
    void init() {
        eventBus.register(this);
    }

    @Subscribe
    public void onEvent(ProductEvent productEvent) {
        this.handle(productEvent.getCtx());
    }

    @Subscribe
    public void onEvent(ProductVariantEvent productVariantEvent) {
        this.handle(productVariantEvent.getCtx());
    }

    private void handle(RequestContext ctx) {
        List<Long> collectionIds = collectionEntityMapper.selectList(null)
                .stream().map(CollectionEntity::getId).collect(Collectors.toList());

        ApplyCollectionFilterEvent applyCollectionFilterEvent =
                new ApplyCollectionFilterEvent(ctx, collectionIds);
        this.eventBus.post(applyCollectionFilterEvent);
    }
}
