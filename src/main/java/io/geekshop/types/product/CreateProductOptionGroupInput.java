/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.product;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class CreateProductOptionGroupInput {
    private String code;
    private String name;
    private List<CreateGroupOptionInput> options = new ArrayList<>();
}
