/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.customer;

import co.jueyi.geekshop.types.common.Node;
import lombok.Data;

import java.util.Date;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class CustomerGroup implements Node {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private String name;
    private CustomerList customers;

    public CustomerList getCustomers(CustomerListOptions options) {
        return null; // TODO
    }
}
