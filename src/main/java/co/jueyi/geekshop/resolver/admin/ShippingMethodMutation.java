/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.custom.security.Allow;
import co.jueyi.geekshop.entity.ShippingMethodEntity;
import co.jueyi.geekshop.service.ShippingMethodService;
import co.jueyi.geekshop.types.common.DeletionResponse;
import co.jueyi.geekshop.types.common.Permission;
import co.jueyi.geekshop.types.shipping.CreateShippingMethodInput;
import co.jueyi.geekshop.types.shipping.ShippingMethod;
import co.jueyi.geekshop.types.shipping.UpdateShippingMethodInput;
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
