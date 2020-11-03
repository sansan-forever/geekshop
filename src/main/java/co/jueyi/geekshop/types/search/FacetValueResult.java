package co.jueyi.geekshop.types.search;

import co.jueyi.geekshop.types.facet.FacetValue;
import lombok.Data;

/**
 * Which FacetValue are present in the products returned
 * by the search, and in what quantity.
 *
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class FacetValueResult {
    public FacetValue facetValue;
    public Integer count;
}
