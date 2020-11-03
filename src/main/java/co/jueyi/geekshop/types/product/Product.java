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
    public Long id;
    public Date createdAt;
    public Date updatedAt;
    public String name;
    public String slug;
    public String description;
    public Asset featuredAsset;
    public List<Asset> assets = new ArrayList<>();
    public List<ProductVariant> variants = new ArrayList<>();
    public List<ProductOptionGroup> optionGroups = new ArrayList<>();
    public List<FacetValue> facetValues = new ArrayList<>();
    public List<Collection> collections = new ArrayList<>();
}
