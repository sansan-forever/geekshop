/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.exception;

import co.jueyi.geekshop.options.OrderOptions;

/**
 * This exception should be thrown when the number or OrderItems in an Order exceeds the limit
 * specified by the `orderItemsLimit` setting in {@link OrderOptions}
 *
 * Created on Dec, 2020 by @author bobo
 */
public class OrderItemsLimitException extends AbstractGraphqlException {
    public OrderItemsLimitException(int maxItems) {
        super(String.format("Cannot add items. An order may consist of a maximum of { %d } items", maxItems),
                ErrorCode.ORDER_ITEMS_LIMIT_EXCEEDED);
    }
}
