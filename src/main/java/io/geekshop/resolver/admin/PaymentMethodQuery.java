/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.resolver.admin;

import io.geekshop.common.utils.BeanMapper;
import io.geekshop.custom.security.Allow;
import io.geekshop.entity.PaymentMethodEntity;
import io.geekshop.service.PaymentMethodService;
import io.geekshop.types.common.Permission;
import io.geekshop.types.payment.PaymentMethod;
import io.geekshop.types.payment.PaymentMethodList;
import io.geekshop.types.payment.PaymentMethodListOptions;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class PaymentMethodQuery implements GraphQLQueryResolver {

    private final PaymentMethodService paymentMethodService;

    @Allow(Permission.ReadSettings)
    public PaymentMethod paymentMethod(Long id, DataFetchingEnvironment dfe)  {
        PaymentMethodEntity paymentMethodEntity =  paymentMethodService.findOne(id);
        if (paymentMethodEntity == null) return null;
        return BeanMapper.map(paymentMethodEntity, PaymentMethod.class);
    }

    @Allow(Permission.ReadSettings)
    public PaymentMethodList paymentMethods(PaymentMethodListOptions options, DataFetchingEnvironment dfe) {
        return paymentMethodService.findAll(options);
    }
}
