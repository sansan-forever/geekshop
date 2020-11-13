/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.shop;

import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.entity.CustomerEntity;
import co.jueyi.geekshop.service.CustomerService;
import co.jueyi.geekshop.types.customer.Customer;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class ShopCustomerQueryResolver implements GraphQLQueryResolver {
    private final CustomerService customerService;

    public Customer activeCustomer(DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        Long userId = ctx.getActiveUserId();
        if (userId != null) {
            CustomerEntity customerEntity = this.customerService.findOneEntityByUserId(userId);
            if (customerEntity == null) return null;
            return BeanMapper.map(customerEntity, Customer.class);
        }
        return null;
    }


}
