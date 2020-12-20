/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.order;

import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class UpdateOrderNoteInput {
    private Long noteId;
    private String note;
    private Boolean privateOnly;
}
