/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.shipping;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class ShippingMethodQuote {
    private Long id;
    private Integer price;
    private String description;
    private Map<String, String> metadata = new HashMap<>();
}
