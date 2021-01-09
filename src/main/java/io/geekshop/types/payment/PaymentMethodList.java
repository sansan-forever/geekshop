/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.payment;

import io.geekshop.types.common.PaginatedList;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Data
public class PaymentMethodList implements PaginatedList<PaymentMethod> {
    private List<PaymentMethod> items = new ArrayList<>();
    private Integer totalItems;
}
