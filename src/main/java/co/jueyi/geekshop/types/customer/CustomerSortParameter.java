package co.jueyi.geekshop.types.customer;

import co.jueyi.geekshop.types.common.SortOrder;
import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class CustomerSortParameter {
    private SortOrder id;
    private SortOrder createdAt;
    private SortOrder updatedAt;
    private SortOrder firstName;
    private SortOrder lastName;
    private SortOrder phoneNumber;
    private SortOrder emailAddress;
}
