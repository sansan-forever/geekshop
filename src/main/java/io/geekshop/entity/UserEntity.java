/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * A User represents any authenticated user of the GeekShop API. This includes both
 * {@link io.geekshop.types.administrator.Administrator}'s as well as registered
 * {@link io.geekshop.types.customer.Customer}
 *
 * Created on Nov, 2020 by @author bobo
 */
@TableName(value = "tb_user")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends BaseEntity {
    private Date deletedAt;
    private String identifier;
    private boolean verified;
    private Date lastLogin;
}
