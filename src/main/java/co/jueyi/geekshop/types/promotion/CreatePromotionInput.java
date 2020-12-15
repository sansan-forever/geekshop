/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.promotion;

import co.jueyi.geekshop.types.common.ConfigurableOperationInput;
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
