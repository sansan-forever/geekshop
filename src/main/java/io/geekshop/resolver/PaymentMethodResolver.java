/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.resolver;

import io.geekshop.service.PaymentMethodService;
import io.geekshop.types.common.ConfigurableOperationDefinition;
import io.geekshop.types.payment.PaymentMethod;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class PaymentMethodResolver implements GraphQLResolver<PaymentMethod> {

    private final PaymentMethodService paymentMethodService;

    public ConfigurableOperationDefinition getDefinition(PaymentMethod paymentMethod, DataFetchingEnvironment dfe) {
        return this.paymentMethodService.getPaymentMethodHandler(paymentMethod.getCode()).toGraphQLType();
    }
}
