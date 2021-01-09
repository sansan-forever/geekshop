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
import io.geekshop.types.payment.UpdatePaymentMethodInput;
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
