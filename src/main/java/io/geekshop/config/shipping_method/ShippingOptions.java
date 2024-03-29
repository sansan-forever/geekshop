/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.config.shipping_method;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Configures the available checkers and calculators for ShippingMethods.
 *
 * Created on Dec, 2020 by @author bobo
 */
@Getter
@RequiredArgsConstructor
public class ShippingOptions {
    private final List<ShippingEligibilityChecker> shippingEligibilityCheckers;
    private final List<ShippingCalculator> shippingCalculators;
}
