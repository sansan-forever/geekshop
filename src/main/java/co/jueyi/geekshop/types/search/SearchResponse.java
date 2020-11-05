package co.jueyi.geekshop.types.search;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class SearchResponse {
    private List<SearchResult> items = new ArrayList<>();
    private Integer totalItems;
    private List<FacetValueResult> facetValues = new ArrayList<>();
}
