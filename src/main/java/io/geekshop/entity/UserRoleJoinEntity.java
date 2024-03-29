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
@TableName("tb_user_role_join")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserRoleJoinEntity extends BaseEntity {
    private Long userId;
    private Long roleId;
}
