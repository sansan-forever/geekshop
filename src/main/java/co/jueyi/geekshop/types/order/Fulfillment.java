/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.order;

import co.jueyi.geekshop.types.common.Node;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class Fulfillment implements Node {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private List<OrderItem> orderItems = new ArrayList<>();
    private String method;
    private String trackingCode;
}
