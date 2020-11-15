/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.custom.security.Allow;
import co.jueyi.geekshop.entity.CustomerGroupEntity;
import co.jueyi.geekshop.service.CustomerGroupService;
import co.jueyi.geekshop.types.common.Permission;
import co.jueyi.geekshop.types.customer.CustomerGroup;
import co.jueyi.geekshop.types.customer.CustomerGroupList;
import co.jueyi.geekshop.types.customer.CustomerGroupListOptions;
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
