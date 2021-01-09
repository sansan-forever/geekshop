/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.mapper;

import io.geekshop.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Mapper
public interface OrderItemEntityMapper extends BaseMapper<OrderItemEntity> {
}
