/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.config.order;

import io.geekshop.common.RequestContext;

/**
 * Allows a user-defined function to create Order codes. This can be useful when
 * integrating with existing systems. By default, GeekShop will generate a 16-character
 * alphanumeric string.
 *
 * Note: When using a using a custom function for Order codes, bear in mind the database limit
 * for string types (e.g. 255 chars for a varchar field in MySQL), and also the need
 * for codes to be unique.
 *
 * Created on Dec, 2020 by @author bobo
 */
public interface OrderCodeGenerator {
    String generate(RequestContext ctx);
}
