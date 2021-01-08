/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.types.product;

import io.geekshop.types.common.ListOptions;
import io.geekshop.types.stock.StockMovementType;
import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class StockMovementListOptions implements ListOptions {
    private Integer currentPage;
    private Integer pageSize;
    private StockMovementType type;
}
