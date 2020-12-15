/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.custom.security.Allow;
import co.jueyi.geekshop.entity.ShippingMethodEntity;
import co.jueyi.geekshop.service.OrderTestingService;
import co.jueyi.geekshop.service.ShippingMethodService;
import co.jueyi.geekshop.types.common.ConfigurableOperationDefinition;
import co.jueyi.geekshop.types.common.Permission;
import co.jueyi.geekshop.types.shipping.*;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class ShippingMethodQuery implements GraphQLQueryResolver {

    private final ShippingMethodService shippingMethodService;
    private final OrderTestingService orderTestingService;

    @Allow(Permission.ReadSettings)
    public ShippingMethodList shippingMethods(ShippingMethodListOptions options, DataFetchingEnvironment dfe) {
        return this.shippingMethodService.findAll(options);
    }

    @Allow(Permission.ReadSettings)
    public ShippingMethod shippingMethod(Long id, DataFetchingEnvironment dfe) {
        ShippingMethodEntity shippingMethodEntity = this.shippingMethodService.findOne(id);
        if (shippingMethodEntity == null) return null;
        return BeanMapper.map(shippingMethodEntity, ShippingMethod.class);
    }

    @Allow(Permission.ReadSettings)
    public List<ConfigurableOperationDefinition> shippingEligibilityCheckers(DataFetchingEnvironment dfe) {
        return this.shippingMethodService.getShippingEligibilityCheckers();
    }

    @Allow(Permission.ReadSettings)
    public List<ConfigurableOperationDefinition> shippingCalculators(DataFetchingEnvironment dfe) {
        return this.shippingMethodService.getShippingEligibilityCheckers();
    }

    @Allow(Permission.ReadSettings)
    public TestShippingMethodResult testShippingMethod(TestShippingMethodInput input, DataFetchingEnvironment dfe) {
        return this.orderTestingService.testShippingMethod(input);
    }

    public List<ShippingMethodQuote> testEligibleShippingMethods(
            TestEligibleShippingMethodsInput input, DataFetchingEnvironment dfe) {
        return this.orderTestingService.testEligibleShippingMethods(input);
    }
}
