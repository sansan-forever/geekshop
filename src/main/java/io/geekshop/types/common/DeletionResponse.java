/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.common;

import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class DeletionResponse {
    private DeletionResult result;
    private String message;
}
