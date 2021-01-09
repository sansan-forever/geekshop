/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.customer;

import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class UpdateCustomerNoteInput {
    private Long noteId;
    private String note;
}
