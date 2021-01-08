/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.resolver.shop;

import io.geekshop.common.RequestContext;
import io.geekshop.common.utils.BeanMapper;
import io.geekshop.entity.CustomerEntity;
import io.geekshop.service.CustomerService;
import io.geekshop.types.customer.Customer;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class ShopCustomerQuery implements GraphQLQueryResolver {
    private final CustomerService customerService;

    public Customer activeCustomer(DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        Long userId = ctx.getActiveUserId();
        if (userId != null) {
            CustomerEntity customerEntity = this.customerService.findOneByUserId(userId);
            if (customerEntity == null) return null;
            return BeanMapper.map(customerEntity, Customer.class);
        }
        return null;
    }


}
