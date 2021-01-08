/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.types.product;

import io.geekshop.types.common.Node;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class ProductOptionGroup implements Node {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private String code;
    private String name;
    private List<ProductOption> options;
}
