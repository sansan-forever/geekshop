/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.data_import.parser;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class ParsedProductWithVariants {
    private ParsedProduct product;
    private List<ParsedProductVariant> variants = new ArrayList<>();
}
