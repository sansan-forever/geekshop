/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.config.collection;

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
