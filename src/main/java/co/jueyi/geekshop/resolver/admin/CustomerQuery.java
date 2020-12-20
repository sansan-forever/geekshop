/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.custom.security.Allow;
import co.jueyi.geekshop.entity.CustomerEntity;
import co.jueyi.geekshop.service.CustomerService;
import co.jueyi.geekshop.types.common.Permission;
import co.jueyi.geekshop.types.customer.Customer;
import co.jueyi.geekshop.types.customer.CustomerList;
import co.jueyi.geekshop.types.customer.CustomerListOptions;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class CustomerQuery implements GraphQLQueryResolver {
    private final CustomerService customerService;

    @Allow(Permission.ReadCustomer)
    public CustomerList customers(CustomerListOptions options, DataFetchingEnvironment dfe) {
        return this.customerService.findAll(options);
    }

    @Allow(Permission.ReadCustomer)
    public Customer customer(Long id, DataFetchingEnvironment dfe) {
        CustomerEntity customerEntity =  this.customerService.findOne(id);
        if (customerEntity == null) return null;
        return BeanMapper.map(customerEntity, Customer.class);
    }
}
