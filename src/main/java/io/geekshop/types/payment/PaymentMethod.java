/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.types.payment;

import io.geekshop.types.common.ConfigArg;
import io.geekshop.types.common.ConfigurableOperationDefinition;
import io.geekshop.types.common.Node;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class PaymentMethod implements Node {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private String code;
    private Boolean enabled;
    private List<ConfigArg> configArgs = new ArrayList<>();
    private ConfigurableOperationDefinition definition;
}
