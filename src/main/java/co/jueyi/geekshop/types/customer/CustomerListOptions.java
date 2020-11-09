package co.jueyi.geekshop.types.customer;

import co.jueyi.geekshop.types.common.ListOptions;
import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class CustomerListOptions implements ListOptions {
    private Integer currentPage;
    private Integer pageSize;
    private CustomerSortParameter sort;
    private CustomerFilterParameter filter;
}
