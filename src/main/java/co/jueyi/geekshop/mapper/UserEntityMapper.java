/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.mapper;

import co.jueyi.geekshop.entity.UserEntity;
import co.jueyi.geekshop.types.user.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Mapper
public interface UserEntityMapper extends BaseMapper<UserEntity> {
}
