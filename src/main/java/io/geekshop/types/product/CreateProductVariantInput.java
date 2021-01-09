/*
 * Copyright (c) 2020 GeekXYZ.
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
public class CreateProductVariantInput {
    private Long productId;
    private String name;
    private List<Long> facetValueIds = new ArrayList<>();
    private String sku;
    private Integer price;
    private List<Long> optionIds = new ArrayList<>();
    private Long featuredAssetId;
    private List<Long> assetIds = new ArrayList<>();
    private Integer stockOnHand;
    private Boolean trackInventory;
}
