/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.service.helpers.refund_state_machine;

/**
 * These are the default states of the refund process.
 *
 * Created on Dec, 2020 by @author bobo
 */
public enum RefundState {
    Pending,
    Settled,
    Failed
}
