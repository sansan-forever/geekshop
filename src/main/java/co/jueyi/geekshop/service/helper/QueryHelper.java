package co.jueyi.geekshop.service.helper;

import co.jueyi.geekshop.types.common.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.util.StringUtils;

/**
 * Created on Nov, 2020 by @author bobo
 */
public abstract class QueryHelper {
    public static void buildOneStringOperatorFilter(QueryWrapper queryWrapper, StringOperators stringOperators,
                                                    String fieldName) {
        if (stringOperators == null) return;
        if (!StringUtils.isEmpty(stringOperators.getEq())) {
            queryWrapper.eq(fieldName, stringOperators.getEq());
        } else if (!StringUtils.isEmpty(stringOperators.getContains())) {
            queryWrapper.like(fieldName, stringOperators.getContains().trim());
        }
    }

    public static void buildOneDateOperatorFilter(QueryWrapper queryWrapper, DateOperators dateOperators,
                                                  String fieldName) {
        if (dateOperators == null) return;
        if (dateOperators.getEq() != null) {
            queryWrapper.eq(fieldName, dateOperators.getEq());
        } else if (dateOperators.getBefore() != null) {
            queryWrapper.le(fieldName, dateOperators.getBefore());
        } else if (dateOperators.getAfter() != null) {
            queryWrapper.gt(fieldName, dateOperators.getAfter());
        } else if (dateOperators.getBetween() != null) {
            DateRange dateRange = dateOperators.getBetween();
            if (dateRange.getStart() != null && dateRange.getEnd() != null) {
                queryWrapper.between(fieldName, dateRange.getStart(), dateRange.getEnd());
            }
        }
    }

    public static void buildOneBooleanOperatorFilter(QueryWrapper queryWrapper, BooleanOperators booleanOperators,
                                                     String fieldName) {
        if (booleanOperators == null) return;
        if (booleanOperators.getEq() != null) {
            queryWrapper.eq(fieldName, booleanOperators.getEq());
        }
    }

    public static void buildOneSortOrder(QueryWrapper queryWrapper, SortOrder sortOrder, String fieldName) {
        if (sortOrder == null) return;
        if (sortOrder == SortOrder.ASC) {
            queryWrapper.orderByAsc(fieldName);
        } else if (sortOrder == SortOrder.DESC) {
            queryWrapper.orderByDesc(fieldName);
        }
    }
}
