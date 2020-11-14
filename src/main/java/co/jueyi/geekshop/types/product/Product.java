/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.product;

import co.jueyi.geekshop.types.asset.Asset;
import co.jueyi.geekshop.types.collection.Collection;
import co.jueyi.geekshop.types.common.Node;
import co.jueyi.geekshop.types.facet.FacetValue;
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
    private String name;
    private String slug;
    private String description;
    private Asset featuredAsset;
    private List<Asset> assets = new ArrayList<>();
    private List<ProductVariant> variants = new ArrayList<>();
    private List<ProductOptionGroup> optionGroups = new ArrayList<>();
    private List<FacetValue> facetValues = new ArrayList<>();
    private List<Collection> collections = new ArrayList<>();
}
