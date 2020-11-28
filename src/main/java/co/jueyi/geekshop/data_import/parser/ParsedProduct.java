/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.data_import.parser;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class ParsedProduct {
    private String name;
    private String slug;
    private String description;
    private List<String> assetPaths = new ArrayList<>();
    private List<StringOptionGroup> optionGroups = new ArrayList<>();
    private List<StringFacet> facets = new ArrayList<>();
}
