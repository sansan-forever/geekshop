/*
 * Copyright (c) 2020 极客之道(daogeek.io).
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
public class CollectionDefinition {
    private String name;
    private String description;
    private String slug;
    private boolean privateOnly;
    private List<FacetValueCollectionFilterDefinition> filters = new ArrayList<>();
    private String parentName;
    private List<String> assetPaths = new ArrayList<>();
}
