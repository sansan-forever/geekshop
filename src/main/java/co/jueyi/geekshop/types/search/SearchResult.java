package co.jueyi.geekshop.types.search;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class SearchResult {
    public String sku;
    public String slug;
    public Long productId;
    public String productName;
    public SearchResultAsset productAsset;
    public Long productVariantId;
    public String productVariantName;
    public SearchResultAsset productVariantAsset;
    /**
     * price和priceRange二选一
     */
    public SinglePrice price;
    public PriceRange priceRange;
    public String description;
    public List<Long> facetIds = new ArrayList<>();
    public List<Long> facetValueIds = new ArrayList<>();
    /**
     * An array of ids of the Collections in which this result appears.
     */
    public List<Long> collectionIds = new ArrayList<>();
    /**
     * A relevence score for the result. Differs between database implementations
     */
    public Float score;
}
