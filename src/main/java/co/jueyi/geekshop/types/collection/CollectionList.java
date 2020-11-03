package co.jueyi.geekshop.types.collection;

import co.jueyi.geekshop.types.common.PaginatedList;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class CollectionList implements PaginatedList<Collection> {
    public List<Collection> items = new ArrayList<>();
    public Integer totalItems;
}
