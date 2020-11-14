/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.administrator;

import co.jueyi.geekshop.types.common.ListOptions;
import lombok.Data;

import javax.validation.constraints.Min;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class AdministratorListOptions implements ListOptions {
    @Min(1)
    private Integer currentPage;
    @Min(1)
    private Integer pageSize;
    private AdministratorSortParameter sort;
    private AdministratorFilterParameter filter;
}
