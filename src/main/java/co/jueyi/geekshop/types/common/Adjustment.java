/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.common;

import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class Adjustment {
    private String adjustmentSource;
    private AdjustmentType type;
    private String description;
    private Integer amount;
}
