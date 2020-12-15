/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.config.order;

import lombok.Builder;
import lombok.Data;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Data
@Builder
public class SimpleLine {
    private Long productVariantId;
    private Integer quantity;
    private Long lineId;
}
