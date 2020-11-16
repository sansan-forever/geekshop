/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.facet;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class CreateFacetInput {
    private String code;
    private String name;
    private Boolean visibleToPublic;
    private List<CreateFacetValueWithFacetInput> values = new ArrayList<>();
}
