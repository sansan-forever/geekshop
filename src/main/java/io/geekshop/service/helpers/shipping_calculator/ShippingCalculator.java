/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.service.helpers.shipping_calculator;

import io.geekshop.config.shipping_method.ShippingCalculationResult;
import io.geekshop.entity.OrderEntity;
import io.geekshop.entity.ShippingMethodEntity;
import io.geekshop.service.ShippingMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class ShippingCalculator {
    private final ShippingMethodService shippingMethodService;

    /**
     * Returns a list of each eligible ShippingMethod for the given Order and sorts them by
     * price, with the cheapest first.
     */
    public List<EligibleShippingMethod> getEligibleShippingMethods(OrderEntity orderEntity) {
        List<ShippingMethodEntity> shippingMethodEntities = this.shippingMethodService.getActiveShippingMethods();

        List<EligibleShippingMethod> eligibleMethods = shippingMethodEntities.stream()
                .map(method -> this.checkEligibilityByShippingMethod(orderEntity, method))
                .collect(Collectors.toList());

        return eligibleMethods.stream().filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(a -> a.getResult().getPrice()))
                .collect(Collectors.toList());
    }

    private EligibleShippingMethod checkEligibilityByShippingMethod(
            OrderEntity orderEntity, ShippingMethodEntity shippingMethodEntity) {
        boolean eligible = shippingMethodEntity.test(orderEntity);
        if (eligible) {
            ShippingCalculationResult result = shippingMethodEntity.apply(orderEntity);
            if (result != null) {
                EligibleShippingMethod eligibleShippingMethod = new EligibleShippingMethod();
                eligibleShippingMethod.setMethod(shippingMethodEntity);
                eligibleShippingMethod.setResult(result);
                return  eligibleShippingMethod;
            }
        }
        return null;
    }
}
