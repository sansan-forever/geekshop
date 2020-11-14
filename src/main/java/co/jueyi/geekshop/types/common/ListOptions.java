/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.common;

/**
 * Created on Nov, 2020 by @author bobo
 */
public interface ListOptions {
    Integer getCurrentPage();
    void setCurrentPage(Integer currentPage);
    Integer getPageSize();
    void setPageSize(Integer pageSize);
}
