/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.service.helpers.order_state_machine;

/**
 * These are the default states of the Order process.
 *
 * Created on Dec, 2020 by @author bobo
 */
public enum OrderState {
    AddingItems,
    ArrangingPayment,
    PaymentAuthorized,
    PaymentSettled,
    PartiallyFulfilled,
    Fulfilled,
    Cancelled
}
