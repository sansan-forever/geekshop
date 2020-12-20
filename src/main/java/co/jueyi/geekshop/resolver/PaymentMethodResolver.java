/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver;

import co.jueyi.geekshop.service.PaymentMethodService;
import co.jueyi.geekshop.types.common.ConfigurableOperationDefinition;
import co.jueyi.geekshop.types.payment.PaymentMethod;
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
