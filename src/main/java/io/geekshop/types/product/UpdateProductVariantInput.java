/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.types.product;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class UpdateProductVariantInput {
    private Long id;
    private Boolean enabled;
    private String name;
    private List<Long> facetValueIds = new ArrayList<>();
    private String sku;
    private Integer price;
    private Long featuredAssetId;
    private List<Long> assetIds = new ArrayList<>();
    private Integer stockOnHand;
    private Boolean trackInventory;
}
