package co.jueyi.geekshop.types.common;

import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
public interface PaginatedList {
    List<Node> getItems();
    Integer getTotalItems();
}
