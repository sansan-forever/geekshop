/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created on Nov, 2020 by @author bobo
 */
@TableName("tb_customer_group_join")
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerGroupJoinEntity extends BaseEntity {
    private Long customerId;
    private Long groupId;
}
