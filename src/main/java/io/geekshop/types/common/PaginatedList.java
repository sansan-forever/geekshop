/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.common;

import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
public interface PaginatedList<T extends Node> {
    List<T> getItems();
    void setItems(List<T> items);
    Integer getTotalItems();
    void setTotalItems(Integer totalItems);
}
