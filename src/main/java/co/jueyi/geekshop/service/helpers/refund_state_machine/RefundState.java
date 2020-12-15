/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.service.helpers.refund_state_machine;

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
