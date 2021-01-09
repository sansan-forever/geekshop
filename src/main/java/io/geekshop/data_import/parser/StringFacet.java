/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.data_import.parser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StringFacet {
    private String facet;
    private String value;
}
