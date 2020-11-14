/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.common;

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
