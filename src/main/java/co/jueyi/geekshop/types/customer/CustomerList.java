package co.jueyi.geekshop.types.customer;

import co.jueyi.geekshop.types.common.PaginatedList;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class CustomerList implements PaginatedList<Customer> {
    public List<Customer> items = new ArrayList<>();
    public Integer totalItems;
}
