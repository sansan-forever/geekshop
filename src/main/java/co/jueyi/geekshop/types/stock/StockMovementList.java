/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.stock;

import co.jueyi.geekshop.types.common.PaginatedList;
import lombok.Data;

import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class StockMovementList implements PaginatedList<StockMovement> {
    private List<StockMovement> items;
    private Integer totalItems;
}
