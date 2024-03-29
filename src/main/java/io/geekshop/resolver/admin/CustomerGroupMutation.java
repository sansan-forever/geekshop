/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.resolver.admin;

import io.geekshop.common.RequestContext;
import io.geekshop.common.utils.BeanMapper;
import io.geekshop.entity.CustomerGroupEntity;
import io.geekshop.service.CustomerGroupService;
import io.geekshop.types.common.DeletionResponse;
import io.geekshop.types.customer.CreateCustomerGroupInput;
import io.geekshop.types.customer.CustomerGroup;
import io.geekshop.types.customer.UpdateCustomerGroupInput;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class CustomerGroupMutation implements GraphQLMutationResolver {

    private final CustomerGroupService customerGroupService;

    /**
     * Create a new CustomerGroup
     */
    public CustomerGroup createCustomerGroup(CreateCustomerGroupInput input, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        CustomerGroupEntity customerGroupEntity = this.customerGroupService.create(ctx, input);
        return BeanMapper.map(customerGroupEntity, CustomerGroup.class);
    }

    /**
     * Update an existing CustomerGroup
     */
    public CustomerGroup updateCustomerGroup(UpdateCustomerGroupInput input, DataFetchingEnvironment dfe) {
        CustomerGroupEntity customerGroupEntity = this.customerGroupService.update(input);
        return BeanMapper.map(customerGroupEntity, CustomerGroup.class);
    }

    /**
     * Delete a CustomerGroup
     */
    public DeletionResponse deleteCustomerGroup(Long id, DataFetchingEnvironment dfe) {
        return this.customerGroupService.delete(id);
    }

    /**
     * Add Customers to a CustomerGroup
     */
    public CustomerGroup addCustomersToGroup(
            Long customerGroupId, List<Long> customerIds, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        CustomerGroupEntity customerGroupEntity =
                this.customerGroupService.addCustomersToGroup(ctx, customerGroupId, customerIds);
        return BeanMapper.map(customerGroupEntity, CustomerGroup.class);
    }

    /**
     * Remove Customers from a CustomerGroup
     */
    public CustomerGroup removeCustomersFromGroup(
            Long customerGroupId, List<Long> customerIds, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        CustomerGroupEntity customerGroupEntity =
                this.customerGroupService.removeCustomersFromGroup(ctx, customerGroupId, customerIds);
        return BeanMapper.map(customerGroupEntity, CustomerGroup.class);
    }
}
