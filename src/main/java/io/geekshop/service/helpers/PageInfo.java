/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.service.helpers;

import lombok.Builder;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Builder
public class PageInfo {
    public Integer current;
    public Integer size;
}
