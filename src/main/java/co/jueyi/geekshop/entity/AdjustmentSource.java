/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.entity;

import co.jueyi.geekshop.types.common.Adjustment;
import co.jueyi.geekshop.types.common.AdjustmentType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created on Dec, 2020 by @author bobo
 */
@Getter
@Setter
public abstract class AdjustmentSource extends BaseEntity {
    @TableField(exist = false)
    private AdjustmentType type;

    public String getSourceId() {
        return this.type + ":" +super.getId();
    }

    public static SourceData decodeSourceId(String sourceId) {
        String[] strings = sourceId.split(":");
        SourceData sourceData = new SourceData();
        sourceData.setType(AdjustmentType.valueOf(strings[0]));
        sourceData.setId(Long.parseLong(strings[1]));
        return sourceData;
    }

    public abstract boolean test(Object...args);

    public abstract Adjustment apply(Object... args);

    @Data
    public static class SourceData {
        private AdjustmentType type;
        private Long id;
    }
}