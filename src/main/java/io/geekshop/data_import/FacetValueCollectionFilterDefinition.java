/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.data_import;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class FacetValueCollectionFilterDefinition {
    private String code = "facet-value-filter";
    private List<String> facetValueNames = new ArrayList<>();
    private boolean containsAny;
}
