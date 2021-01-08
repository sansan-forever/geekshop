/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.types.search;

import io.geekshop.types.facet.FacetValue;
import lombok.Data;

/**
 * Which FacetValue are present in the products returned
 * by the search, and in what quantity.
 *
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class FacetValueResult {
    private FacetValue facetValue;
    private Integer count;
}
