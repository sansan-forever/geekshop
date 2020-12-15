/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.shipping;

import co.jueyi.geekshop.types.common.ListOptions;
import lombok.Data;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Data
public class ShippingMethodListOptions implements ListOptions {
    private Integer currentPage;
    private Integer pageSize;
    private ShippingMethodSortParameter sort;
    private ShippingMethodFilterParameter filter;
}
