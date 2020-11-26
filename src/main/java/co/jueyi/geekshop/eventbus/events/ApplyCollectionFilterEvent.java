/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.eventbus.events;

import co.jueyi.geekshop.common.RequestContext;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApplyCollectionFilterEvent extends BaseEvent {
    private final RequestContext ctx;
    private final List<Long> collectionIds;
}
