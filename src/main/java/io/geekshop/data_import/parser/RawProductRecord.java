/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.data_import.parser;

import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class RawProductRecord {
    private String name;
    private String slug;
    private String description;
    private String assets;
    private String facets;
    private String optionGroups;
    private String optionValues;
    private String sku;
    private String price;
    private String sockOnHand;
    private String trackInventory;
    private String variantAssets;
    private String variantFacets;
}
