/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.types.shipping;

import io.geekshop.types.common.ConfigurableOperation;
import io.geekshop.types.common.Node;
import lombok.Data;

import java.util.Date;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class ShippingMethod implements Node {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private String code;
    private String description;
    private ConfigurableOperation checker;
    private ConfigurableOperation calculator;
}
