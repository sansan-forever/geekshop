/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.entity;

import co.jueyi.geekshop.types.history.HistoryEntryType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on Nov, 2020 by @author bobo
 */
@TableName(value = "tb_order_history_entry", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderHistoryEntryEntity extends BaseEntity {
    private Long administratorId;
    private HistoryEntryType type;
    private boolean isPublic;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String,String> data = new HashMap<>();
    private Long orderId;
}
