/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.resolver.shop;

import io.geekshop.common.RequestContext;
import io.geekshop.common.utils.BeanMapper;
import io.geekshop.custom.security.Allow;
import io.geekshop.entity.AddressEntity;
import io.geekshop.entity.CustomerEntity;
import io.geekshop.exception.ForbiddenException;
import io.geekshop.exception.InternalServerError;
import io.geekshop.service.CustomerService;
import io.geekshop.types.address.Address;
import io.geekshop.types.common.CreateAddressInput;
import io.geekshop.types.common.Permission;
import io.geekshop.types.common.UpdateAddressInput;
import io.geekshop.types.customer.Customer;
import io.geekshop.types.customer.UpdateCustomerInput;
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
public class ShopCustomerMutation implements GraphQLMutationResolver {
    private final CustomerService customerService;

    @Allow(Permission.Owner)
    public Customer updateCustomer(UpdateCustomerInput input, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        CustomerEntity customerEntity = this.getCustomerForOwner(ctx);
        input.setId(customerEntity.getId());
        customerEntity = this.customerService.update(ctx, input);
        return BeanMapper.map(customerEntity, Customer.class);
    }

    @Allow(Permission.Owner)
    public Address createCustomerAddress(CreateAddressInput input, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        CustomerEntity customerEntity = this.getCustomerForOwner(ctx);
        AddressEntity addressEntity = this.customerService.createAddress(ctx, customerEntity.getId(), input);
        return BeanMapper.map(addressEntity, Address.class);
    }

    @Allow(Permission.Owner)
    public Address updateCustomerAddress(UpdateAddressInput input, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        CustomerEntity customerEntity = this.getCustomerForOwner(ctx);
        List<AddressEntity> customerAddressList =
                this.customerService.findAddressEntitiesByCustomerId(ctx, customerEntity.getId());
        if (!customerAddressList.stream().anyMatch(addressEntity -> addressEntity.getId().equals(input.getId()))) {
            throw new ForbiddenException();
        }
        AddressEntity addressEntity = this.customerService.updateAddress(ctx, input);
        return BeanMapper.map(addressEntity, Address.class);
    }

    @Allow(Permission.Owner)
    public Boolean deleteCustomerAddress(Long id, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        CustomerEntity customerEntity = this.getCustomerForOwner(ctx);
        List<AddressEntity> customerAddressList =
                this.customerService.findAddressEntitiesByCustomerId(ctx, customerEntity.getId());
        if (!customerAddressList.stream().anyMatch(addressEntity -> addressEntity.getId().equals(id))) {
            throw new ForbiddenException();
        }
        return this.customerService.deleteAddress(ctx, id);
    }

    /**
     * Returns the Customer entity associated with the current user.
     */
    private CustomerEntity getCustomerForOwner(RequestContext ctx) {
        Long userId = ctx.getActiveUserId();
        if (userId == null) {
            throw new ForbiddenException();
        }
        CustomerEntity customerEntity = this.customerService.findOneByUserId(userId);
        if (customerEntity == null) {
            throw new InternalServerError("No customer found for current user");
        }
        return customerEntity;
    }
}
