/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.types.facet;

import io.geekshop.types.common.Node;
import lombok.Data;

import java.util.Date;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class FacetValue implements Node {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private Facet facet;
    private Long facetId; // 内部使用，GraphQL不可见
    private String name;
    private String code;
}
