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
public class CreateProductInput {
    private Long featuredAssetId;
    private List<Long> assetIds = new ArrayList<>();
    private List<Long> facetValueIds = new ArrayList<>();
    private String name;
    private String slug;
    private String description;
}
