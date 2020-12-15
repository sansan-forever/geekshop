/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.config.payment_method;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Payment-related options
 *
 * Created on Dec, 2020 by @author bobo
 */
@Getter
@RequiredArgsConstructor
public class PaymentOptions {
    /**
     * A list of {@link PaymentMethodHandler}s with which to process payments.
     */
    private final List<PaymentMethodHandler> paymentMethodHandlers;
}
