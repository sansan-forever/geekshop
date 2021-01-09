/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.payment;

import io.geekshop.types.common.Node;
import io.geekshop.types.order.OrderItem;
import lombok.Data;

import java.util.*;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class Refund implements Node {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private Integer items;
    private Integer shipping;
    private Integer adjustment;
    private Integer total;
    private String method;
    private String state;
    private String transactionId;
    private String reason;
    private List<OrderItem> orderItems = new ArrayList<>();
    private Long paymentId;
    private Map<String, String> metadata = new HashMap<>();
}
