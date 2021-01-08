/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.resolver.admin;

import io.geekshop.common.utils.BeanMapper;
import io.geekshop.custom.security.Allow;
import io.geekshop.entity.CustomerGroupEntity;
import io.geekshop.service.CustomerGroupService;
import io.geekshop.types.common.Permission;
import io.geekshop.types.customer.CustomerGroup;
import io.geekshop.types.customer.CustomerGroupList;
import io.geekshop.types.customer.CustomerGroupListOptions;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class CustomerGroupQuery implements GraphQLQueryResolver {

    private final CustomerGroupService customerGroupService;

    @Allow(Permission.ReadCustomer)
    public CustomerGroupList customerGroups(CustomerGroupListOptions options, DataFetchingEnvironment dfe) {
        return this.customerGroupService.findAll(options);
    }

    @Allow(Permission.ReadCustomer)
    public CustomerGroup customerGroup(Long id, DataFetchingEnvironment dfe) {
        CustomerGroupEntity customerGroupEntity = this.customerGroupService.findOne(id);
        if (customerGroupEntity == null) return null;
        return BeanMapper.map(customerGroupEntity, CustomerGroup.class);
    }
}
