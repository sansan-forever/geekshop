/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.types.collection;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
@AllArgsConstructor
public class CollectionBreadcrumb {
    private Long id;
    private String name;
    private String slug;
}
