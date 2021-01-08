/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.entity;

import io.geekshop.custom.mybatis_plus.ConfigArgListTypeHandler;
import io.geekshop.types.common.ConfigArg;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * A PaymentMethod is created automatically according to the configured {@link PaymentMethodHandler}s defined in the
 * {@link PaymentOptions} config.
 *
 * Created on Dec, 2020 by @author bobo
 */
@TableName(value = "tb_payment_method", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentMethodEntity extends BaseEntity {
    private String code;
    private boolean enabled;
    @TableField(typeHandler = ConfigArgListTypeHandler.class)
    private List<ConfigArg> configArgs = new ArrayList<>();
}
