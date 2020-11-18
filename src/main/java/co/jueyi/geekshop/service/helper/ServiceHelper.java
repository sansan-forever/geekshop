/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.service.helper;

import co.jueyi.geekshop.common.Constant;
import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.entity.AddressEntity;
import co.jueyi.geekshop.exception.EntityNotFoundException;
import co.jueyi.geekshop.service.args.CreateCustomerHistoryEntryArgs;
import co.jueyi.geekshop.service.args.UpdateCustomerHistoryEntryArgs;
import co.jueyi.geekshop.types.common.ListOptions;
import co.jueyi.geekshop.types.history.HistoryEntryType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created on Nov, 2020 by @author bobo
 */
public abstract class ServiceHelper {
    private static String COLUMN_DELETED_AT = "deleted_at";
    private static String FIELD_DELETED_AT = "deletedAt";
    private static String COLUMN_ID = "id";

    public static <T> T getEntityOrThrow(BaseMapper<T> mapper, Class<T> clazz, Long id) {
        QueryWrapper<T> queryWrapper = new QueryWrapper();
        queryWrapper.eq(COLUMN_ID, id);
        boolean supportSoftDelete =
                getAllFields(clazz).contains(FIELD_DELETED_AT);
        if (supportSoftDelete) {
            queryWrapper.isNull(COLUMN_DELETED_AT);
        }
        T entity = mapper.selectOne(queryWrapper);
        if (entity == null) {
            throw new EntityNotFoundException(clazz.getSimpleName(), id);
        }
        return entity;
    }

    private static Set<String> getAllFields(final Class<?> type) {
        return Arrays.stream(type.getDeclaredFields()).map(field -> field.getName()).collect(Collectors.toSet());
    }

    public static PageInfo getListOptions(ListOptions options) {
        int currentPage = Constant.DEFAULT_CURRENT_PAGE;
        if (options != null && options.getCurrentPage() != null) {
            currentPage = options.getCurrentPage();
        }
        int pageSize = Constant.DEFAULT_PAGE_SIZE;
        if (options != null && options.getPageSize() != null) {
            pageSize = options.getPageSize();
        }
        return PageInfo.builder().current(currentPage).size(pageSize).build();
    }

    public static CreateCustomerHistoryEntryArgs buildCreateCustomerHistoryEntryArgs(
            RequestContext ctx, Long customerId, HistoryEntryType type) {
        return buildCreateCustomerHistoryEntryArgs(
                ctx, customerId, type, null
        );
    }

    public static CreateCustomerHistoryEntryArgs buildCreateCustomerHistoryEntryArgs(
            RequestContext ctx, Long customerId, HistoryEntryType type, Map<String, String> data) {
        CreateCustomerHistoryEntryArgs args = new CreateCustomerHistoryEntryArgs();
        args.setCustomerId(customerId);
        args.setCtx(ctx);
        args.setType(type);
        if (data != null && data.size() > 0) {
            args.getData().putAll(data);
        }
        return args;
    }

    public static UpdateCustomerHistoryEntryArgs buildUpdateCustomerHistoryEntryArgs(
            RequestContext ctx, Long entryId, HistoryEntryType type) {
        return buildUpdateCustomerHistoryEntryArgs(
                ctx, entryId, type, null
        );
    }

    public static UpdateCustomerHistoryEntryArgs buildUpdateCustomerHistoryEntryArgs(
            RequestContext ctx, Long entryId, HistoryEntryType type, Map<String, String> data) {
        UpdateCustomerHistoryEntryArgs args = new UpdateCustomerHistoryEntryArgs();
        args.setEntryId(entryId);
        args.setCtx(ctx);
        args.setType(type);
        if (data != null && data.size() > 0) {
            args.getData().putAll(data);
        }
        return args;
    }

    public static String addressToLine(AddressEntity addressEntity) {
        String result = "";
        if (!StringUtils.isEmpty(addressEntity.getStreetLine1())) {
            result += addressEntity.getStreetLine1();
        }
        if (!StringUtils.isEmpty(addressEntity.getPostalCode())) {
            result += ", " + addressEntity.getPostalCode();
        }
        return result;
    }
}
