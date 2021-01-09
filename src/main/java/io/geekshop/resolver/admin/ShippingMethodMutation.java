/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.resolver.admin;

import io.geekshop.common.utils.BeanMapper;
import io.geekshop.custom.security.Allow;
import io.geekshop.entity.ShippingMethodEntity;
import io.geekshop.service.ShippingMethodService;
import io.geekshop.types.common.DeletionResponse;
import io.geekshop.types.common.Permission;
import io.geekshop.types.shipping.CreateShippingMethodInput;
import io.geekshop.types.shipping.ShippingMethod;
import io.geekshop.types.shipping.UpdateShippingMethodInput;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class ShippingMethodMutation implements GraphQLMutationResolver {

    private final ShippingMethodService shippingMethodService;

    @Allow(Permission.CreateSettings)
    public ShippingMethod createShippingMethod(CreateShippingMethodInput input, DataFetchingEnvironment dfe) {
        ShippingMethodEntity shippingMethodEntity = this.shippingMethodService.create(input);
        return BeanMapper.map(shippingMethodEntity, ShippingMethod.class);
    }

    @Allow(Permission.UpdateSettings)
    public ShippingMethod updateShippingMethod(UpdateShippingMethodInput input, DataFetchingEnvironment dfe) {
        ShippingMethodEntity shippingMethodEntity = this.shippingMethodService.update(input);
        return BeanMapper.map(shippingMethodEntity, ShippingMethod.class);
    }

    @Allow(Permission.DeleteSettings)
    public DeletionResponse deleteShippingMethod(Long id, DataFetchingEnvironment dfe) {
        return this.shippingMethodService.softDelete(id);
    }
}
