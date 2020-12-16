/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.custom.security.Allow;
import co.jueyi.geekshop.entity.PaymentMethodEntity;
import co.jueyi.geekshop.service.PaymentMethodService;
import co.jueyi.geekshop.types.common.Permission;
import co.jueyi.geekshop.types.payment.PaymentMethod;
import co.jueyi.geekshop.types.payment.PaymentMethodList;
import co.jueyi.geekshop.types.payment.PaymentMethodListOptions;
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
