/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.types.product;

import io.geekshop.types.asset.Asset;
import io.geekshop.types.collection.Collection;
import io.geekshop.types.common.Node;
import io.geekshop.types.facet.FacetValue;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class Product implements Node {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private Boolean enabled; // admin only
    private String name;
    private String slug;
    private String description;
    private Asset featuredAsset;
    private Long featuredAssetId; // 内部使用，GraphQL不可见
    private List<Asset> assets = new ArrayList<>();
    private List<ProductVariant> variants = new ArrayList<>();
    private List<ProductOptionGroup> optionGroups = new ArrayList<>();
    private List<FacetValue> facetValues = new ArrayList<>();
    private List<Collection> collections = new ArrayList<>();
}
