/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.resolver.admin;

import io.geekshop.common.utils.BeanMapper;
import io.geekshop.custom.security.Allow;
import io.geekshop.entity.CustomerEntity;
import io.geekshop.service.CustomerService;
import io.geekshop.types.common.Permission;
import io.geekshop.types.customer.Customer;
import io.geekshop.types.customer.CustomerList;
import io.geekshop.types.customer.CustomerListOptions;
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
