/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.config.collection;

import lombok.Data;

import java.util.List;

/**
 * Configs related to products and collections.
 *
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class CatalogConfig {
    /**
     * Allows custom {@link CollectionFilter}s to be defined.
     */
    private final List<CollectionFilter> collectionFilters;
}
