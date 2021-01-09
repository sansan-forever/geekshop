/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.eventbus.events;


import io.geekshop.common.RequestContext;
import io.geekshop.entity.AssetEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * This event is fired whenere an {@link io.geekshop.types.asset.Asset} is added, updated
 * or deleted.
 *
 * Created on Nov, 2020 by @author bobo
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AssetEvent extends BaseEvent {
    private final RequestContext ctx;
    private final AssetEntity asset;
    private final String type; // 'created' | 'updated' | 'deleted'
}
