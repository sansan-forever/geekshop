/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.service.helpers.order_merger;

import lombok.Builder;
import lombok.Data;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Data
@Builder
public class LineItem {
    private Long productVariantId;
    private Integer quantity;
}
