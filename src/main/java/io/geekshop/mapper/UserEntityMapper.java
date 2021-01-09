/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.mapper;

import io.geekshop.entity.UserEntity;
import io.geekshop.types.user.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Mapper
public interface UserEntityMapper extends BaseMapper<UserEntity> {
}
