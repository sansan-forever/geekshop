package co.jueyi.geekshop.types.search;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class SearchResponse {
    public List<SearchResult> items = new ArrayList<>();
    public Integer totalItems;
    public List<FacetValueResult> facetValues = new ArrayList<>();
}
