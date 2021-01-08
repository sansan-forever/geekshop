/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.types.shipping;

import io.geekshop.types.common.PaginatedList;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class ShippingMethodList implements PaginatedList<ShippingMethod> {
    private List<ShippingMethod> items = new ArrayList<>();
    private Integer totalItems;
}
