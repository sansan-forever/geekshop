/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.service.helpers.shipping_calculator;

import co.jueyi.geekshop.config.shipping_method.ShippingCalculationResult;
import co.jueyi.geekshop.entity.OrderEntity;
import co.jueyi.geekshop.entity.ShippingMethodEntity;
import co.jueyi.geekshop.service.ShippingMethodService;
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
