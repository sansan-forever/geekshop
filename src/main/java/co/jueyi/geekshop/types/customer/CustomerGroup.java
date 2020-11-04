package co.jueyi.geekshop.types.customer;

import co.jueyi.geekshop.types.common.Node;
import lombok.Data;

import java.util.Date;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class CustomerGroup implements Node {
    public Long id;
    public Date createdAt;
    public Date updatedAt;
    public String name;
    public CustomerList customers;

    public CustomerList getCustomers(CustomerListOptions options) {
        return null; // TODO
    }
}
