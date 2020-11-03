package co.jueyi.geekshop.types.collection;

import co.jueyi.geekshop.types.asset.Asset;
import co.jueyi.geekshop.types.common.ConfigurableOperation;
import co.jueyi.geekshop.types.common.Node;
import co.jueyi.geekshop.types.product.ProductVariantList;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class Collection implements Node {
    public Long id;
    public Date createdAt;
    public Date updatedAt;
    public String name;
    public String slug;
    public List<CollectionBreadcrumb> breadcrumbs = new ArrayList<>();
    public Integer position;
    public String description;
    public Asset featuredAsset;
    public List<Asset> assets = new ArrayList<>();
    public Collection parent;
    public List<Collection> children = new ArrayList<>();
    public List<ConfigurableOperation> filters = new ArrayList<>();
    public ProductVariantList productVariants;
}
