/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.config.order;

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
