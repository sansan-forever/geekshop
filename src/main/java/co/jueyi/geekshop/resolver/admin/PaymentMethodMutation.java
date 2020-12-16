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
import co.jueyi.geekshop.types.payment.UpdatePaymentMethodInput;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class PaymentMethodMutation implements GraphQLMutationResolver {

    private final PaymentMethodService paymentMethodService;

    /**
     * Update an existing PaymentMethod
     */
    @Allow(Permission.UpdateSettings)
    public PaymentMethod updatePaymentMethod(UpdatePaymentMethodInput input, DataFetchingEnvironment dfe) {
        PaymentMethodEntity paymentMethodEntity = this.paymentMethodService.update(input);
        return BeanMapper.map(paymentMethodEntity, PaymentMethod.class);
    }
}
