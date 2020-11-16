/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * A particular value of {@link co.jueyi.geekshop.types.facet.Facet}
 *
 * Created on Nov, 2020 by @author bobo
 */
@TableName(value = "tb_facet_value")
@Data
@EqualsAndHashCode(callSuper = true)
public class FacetValueEntity extends BaseEntity {
    private String name;
    private String code;
    private Long facetId;
}
