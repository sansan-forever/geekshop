/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.types.order;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class CancelOrderInput {
    /**
     * The id of the order to be cancelled
     */
    private Long orderId;
    /**
     * Optionally specify which OrderLines to cancel. If not provided, all OrderLines will be cancelled.
     */
    private List<OrderLineInput> lines;
    private String reason;
}
