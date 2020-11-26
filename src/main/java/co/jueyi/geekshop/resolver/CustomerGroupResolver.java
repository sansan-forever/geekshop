/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver;

import co.jueyi.geekshop.service.CustomerGroupService;
import co.jueyi.geekshop.types.customer.CustomerGroup;
import co.jueyi.geekshop.types.customer.CustomerList;
import co.jueyi.geekshop.types.customer.CustomerListOptions;
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
