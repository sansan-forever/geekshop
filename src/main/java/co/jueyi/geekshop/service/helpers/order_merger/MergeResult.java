/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.service.helpers.order_merger;

import co.jueyi.geekshop.entity.OrderEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Data
@Builder
public class MergeResult {
    private OrderEntity order;
    private List<LineItem> linesToInsert;
    private OrderEntity orderToDelete;
}
