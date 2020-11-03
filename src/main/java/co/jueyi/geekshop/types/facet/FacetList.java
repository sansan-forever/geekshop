package co.jueyi.geekshop.types.facet;

import co.jueyi.geekshop.types.common.PaginatedList;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class FacetList implements PaginatedList<Facet> {
    public List<Facet> items = new ArrayList<>();
    public Integer totalItems;
}
