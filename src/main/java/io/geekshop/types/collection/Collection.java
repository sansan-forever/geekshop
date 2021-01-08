/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.types.collection;

import io.geekshop.types.asset.Asset;
import io.geekshop.types.common.ConfigurableOperation;
import io.geekshop.types.common.Node;
import io.geekshop.types.product.ProductVariantList;
import io.geekshop.types.product.ProductVariantListOptions;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class Collection implements Node {
    private Long id;
    private Boolean privateOnly; // admin only
    private Date createdAt;
    private Date updatedAt;
    private String name;
    private String slug;
    private List<CollectionBreadcrumb> breadcrumbs = new ArrayList<>();
    private Integer position;
    private String description;
    private Asset featuredAsset;
    private Long featuredAssetId; // 仅内部使用，GraphQL对外不可见
    private List<Asset> assets = new ArrayList<>();
    private Collection parent;
    private Long parentId; // 仅内部使用，GraphQL对外不可见
    private List<Collection> children = new ArrayList<>();
    private List<ConfigurableOperation> filters = new ArrayList<>();
    private ProductVariantList productVariants;
}
