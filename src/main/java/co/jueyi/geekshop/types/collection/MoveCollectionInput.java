/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.collection;

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
