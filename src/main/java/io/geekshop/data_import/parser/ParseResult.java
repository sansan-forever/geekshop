/*
 * Copyright (c) 2020 极客之道(daogeek.io).
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
public class ParseResult<T> {
    private List<T> results = new ArrayList<>();
    private List<String> errors = new ArrayList<>();
    private Integer processed;
}
