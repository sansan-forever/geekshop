/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.service.helpers.order_merger;

import io.geekshop.entity.OrderEntity;
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
