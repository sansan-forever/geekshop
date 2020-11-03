package co.jueyi.geekshop.types.order;

import co.jueyi.geekshop.types.common.PaginatedList;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class OrderList implements PaginatedList<Order> {
    public List<Order> items = new ArrayList<>();
    public Integer totalItems;
}
