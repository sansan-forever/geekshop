/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.entity;

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
