/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.payment;

import co.jueyi.geekshop.types.common.ConfigArgInput;
import lombok.Data;

import java.util.List;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Data
public class UpdatePaymentMethodInput {
    private Long id;
    private String code;
    private Boolean enabled;
    private List<ConfigArgInput> configArgs;
}
