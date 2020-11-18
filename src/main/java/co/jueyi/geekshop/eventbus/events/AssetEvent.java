/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.eventbus.events;


import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.entity.AssetEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * This event is fired whenere an {@link co.jueyi.geekshop.types.asset.Asset} is added, updated
 * or deleted.
 *
 * Created on Nov, 2020 by @author bobo
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AssetEvent extends BaseEvent {
    private final RequestContext ctx;
    private final AssetEntity assetEntity;
    private final String type; // 'created' | 'updated' | 'deleted'
}
