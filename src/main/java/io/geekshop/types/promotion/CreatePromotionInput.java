/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.promotion;

import io.geekshop.types.common.ConfigurableOperationInput;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Data
public class CreatePromotionInput {
    private String name;
    private Boolean enabled;
    private Date startsAt;
    private Date endsAt;
    private String couponCode;
    private Integer perCustomerUsageLimit;
    private List<ConfigurableOperationInput> conditions = new ArrayList<>();
    private List<ConfigurableOperationInput> actions = new ArrayList<>();
}
