/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.collection;

import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class MoveCollectionInput {
    private Long collectionId;
    private Long parentId;
    private Integer index;
}
