package co.jueyi.geekshop.types.product;

import co.jueyi.geekshop.types.asset.Asset;
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
public class ProductVariant implements Node {
    public Long id;
    public Product product;
    public Long productId;
    public Date createdAt;
    public Date updatedAt;
    public String sku;
    public String name;
    public Asset featuredAsset;
    public List<Asset> assets = new ArrayList<>();
    public Integer price;
    public List<ProductOption> options = new ArrayList<>();
    public List<FacetValue> facetValues = new ArrayList<>();
}
