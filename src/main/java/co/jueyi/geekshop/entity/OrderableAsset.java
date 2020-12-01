/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * This base class is extended in order to enable specific ordering of the one-to-many
 * Entity -> Assets relation.
 *
 * Created on Nov, 2020 by @author bobo
 */
@Getter
@Setter
public abstract class OrderableAsset extends BaseEntity{
    private Long assetId;
    private Integer position;
}
