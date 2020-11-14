/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.order;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class RefundOrderInput {
    private List<OrderLineInput> lines = new ArrayList<>();
    private Integer shipping;
    private Integer adjustment;
    private Long paymentId;
    private String reason;
}
