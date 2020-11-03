package co.jueyi.geekshop.types.common;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class SearchInput {
    public String term;
    public List<Long> facetValueIds = new ArrayList<>();
    public LogicalOperator facetValueOperator;
    public Long collectionId;
    public String collectionSlug;
    public Boolean groupByProduct;
    public Integer currentPage;
    public Integer pageSize;
}
