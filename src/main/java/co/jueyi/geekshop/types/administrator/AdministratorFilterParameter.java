/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.administrator;

import co.jueyi.geekshop.types.common.DateOperators;
import co.jueyi.geekshop.types.common.StringOperators;
import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class AdministratorFilterParameter {
    private StringOperators firstName;
    private StringOperators lastName;
    private StringOperators emailAddress;
    private DateOperators createdAt;
    private DateOperators updatedAt;
}
