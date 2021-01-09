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
public class UpdateProductInput {
    private Long id;
    private Boolean enabled;
    private Long featuredAssetId;
    private List<Long> assetIds;
    private List<Long> facetValueIds;
    private String name;
    private String slug;
    private String description;
}
