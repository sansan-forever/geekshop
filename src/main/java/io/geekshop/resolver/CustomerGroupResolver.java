/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.resolver;

import io.geekshop.service.CustomerGroupService;
import io.geekshop.types.customer.CustomerGroup;
import io.geekshop.types.customer.CustomerList;
import io.geekshop.types.customer.CustomerListOptions;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class CustomerGroupResolver implements GraphQLResolver<CustomerGroup> {

    private final CustomerGroupService customerGroupService;

    public CustomerList getCustomers(
            CustomerGroup customerGroup, CustomerListOptions options, DataFetchingEnvironment dfe) {
        return this.customerGroupService.getGroupCustomers(customerGroup.getId(), options);
    }
}
