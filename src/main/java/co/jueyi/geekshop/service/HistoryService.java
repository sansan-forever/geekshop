/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.service;

import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.entity.AdministratorEntity;
import co.jueyi.geekshop.entity.CustomerHistoryEntryEntity;
import co.jueyi.geekshop.exception.EntityNotFoundException;
import co.jueyi.geekshop.mapper.CustomerHistoryEntryEntityMapper;
import co.jueyi.geekshop.service.args.CreateCustomerHistoryEntryArgs;
import co.jueyi.geekshop.service.args.UpdateCustomerHistoryEntryArgs;
import co.jueyi.geekshop.service.helper.QueryHelper;
import co.jueyi.geekshop.service.helper.ServiceHelper;
import co.jueyi.geekshop.types.history.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


/**
 * The HistoryService is responsible for creating and retrieving HistoryEntry entities.
 *
 * Created on Nov, 2020 by @author bobo
 */
@Service
@RequiredArgsConstructor
public class HistoryService {
    private final AdministratorService administratorService;
    private final CustomerHistoryEntryEntityMapper customerHistoryEntryEntityMapper;

    public static final String KEY_STRATEGY = "strategy";
    public static final String KEY_OLD_EMAIL_ADDRESS = "oldEmailAddress";
    public static final String KEY_NEW_EMAIL_ADDRESS = "newEmailAddress";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_NOTE = "node";

    @SuppressWarnings("Duplicates")
    public HistoryEntryList getHistoryForCustomer(
            Long customerId, HistoryEntryListOptions options) {
        Pair<Integer, Integer> currentAndSize = ServiceHelper.getListOptions(options);
        IPage<CustomerHistoryEntryEntity> page = new Page<>(currentAndSize.getLeft(), currentAndSize.getRight());
        QueryWrapper<CustomerHistoryEntryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CustomerHistoryEntryEntity::getCustomerId, customerId);
        if (options != null) {
            buildFilter(queryWrapper, options.getFilter());
            buildSortOrder(queryWrapper, options.getSort());
        }
        IPage<CustomerHistoryEntryEntity> customerHistoryEntryEntityPage =
                this.customerHistoryEntryEntityMapper.selectPage(page, queryWrapper);

        HistoryEntryList historyEntryList = new HistoryEntryList();
        historyEntryList.setTotalItems((int) customerHistoryEntryEntityPage.getTotal());

        if (CollectionUtils.isEmpty(customerHistoryEntryEntityPage.getRecords()))
            return historyEntryList; // 返回空

        // 将持久化实体类型转换成GraphQL传输类型
        customerHistoryEntryEntityPage.getRecords().forEach(customerHistoryEntryEntity -> {
            HistoryEntry historyEntry = BeanMapper.map(customerHistoryEntryEntity, HistoryEntry.class);
            historyEntryList.getItems().add(historyEntry);
        });

        return historyEntryList;
    }

    private void buildSortOrder(QueryWrapper queryWrapper, HistoryEntrySortParameter sortParameter) {
        if (sortParameter == null) return ;
        QueryHelper.buildOneSortOrder(queryWrapper, sortParameter.getId(), "id");
        QueryHelper.buildOneSortOrder(queryWrapper, sortParameter.getCreatedAt(), "created_at");
        QueryHelper.buildOneSortOrder(queryWrapper, sortParameter.getUpdatedAt(), "updated_at");
    }

    private void buildFilter(QueryWrapper queryWrapper, HistoryEntryFilterParameter filterParameter) {
        if (filterParameter == null) return;
        QueryHelper.buildOneStringOperatorFilter(queryWrapper, filterParameter.getType(), "type");
        QueryHelper.buildOneBooleanOperatorFilter(queryWrapper, filterParameter.getIsPublic(), "is_public");
        QueryHelper.buildOneDateOperatorFilter(queryWrapper, filterParameter.getCreatedAt(), "created_at");
        QueryHelper.buildOneDateOperatorFilter(queryWrapper, filterParameter.getUpdatedAt(), "updated_at");
    }

    public HistoryEntry createHistoryEntryForCustomer(CreateCustomerHistoryEntryArgs args) {
        return this.createHistoryEntryForCustomer(args, false);
    }

    public HistoryEntry createHistoryEntryForCustomer(CreateCustomerHistoryEntryArgs args, Boolean isPublic) {
        AdministratorEntity administratorEntity =
                this.getAdministratorFromContext(args.getCtx());
        CustomerHistoryEntryEntity customerHistoryEntryEntity =
                BeanMapper.map(args, CustomerHistoryEntryEntity.class);
        customerHistoryEntryEntity.setAdministratorId(administratorEntity == null ? null : administratorEntity.getId());
        customerHistoryEntryEntity.setPublic(BooleanUtils.toBoolean(isPublic));
        this.customerHistoryEntryEntityMapper.insert(customerHistoryEntryEntity);
        return BeanMapper.map(customerHistoryEntryEntity, HistoryEntry.class);
    }

    public HistoryEntry updateCustomerHistoryEntry(UpdateCustomerHistoryEntryArgs args) {
        QueryWrapper<CustomerHistoryEntryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CustomerHistoryEntryEntity::getId, args.getEntryId())
                .eq(CustomerHistoryEntryEntity::getType, args.getType());
        CustomerHistoryEntryEntity customerHistoryEntryEntity =
                this.customerHistoryEntryEntityMapper.selectOne(queryWrapper);
        if (customerHistoryEntryEntity == null) {
            throw new EntityNotFoundException("CustomerHistoryEntry", args.getEntryId());
        }

        if (args.getData() != null && !args.getData().isEmpty()) {
            customerHistoryEntryEntity.setData(args.getData());
        }
        AdministratorEntity administratorEntity = this.getAdministratorFromContext(args.getCtx());
        if (administratorEntity != null) {
            customerHistoryEntryEntity.setCustomerId(administratorEntity.getId());
        }
        this.customerHistoryEntryEntityMapper.updateById(customerHistoryEntryEntity);
        return BeanMapper.map(customerHistoryEntryEntity, HistoryEntry.class);
    }

    public void deleteCustomerHistoryEntry(Long id) {
        // 确保存在
        ServiceHelper.getEntityOrThrow(this.customerHistoryEntryEntityMapper, id);
        this.customerHistoryEntryEntityMapper.deleteById(id);
    }

    private AdministratorEntity getAdministratorFromContext(RequestContext ctx) {
        if (ctx.getActiveUserId() == null) return null;

        return this.administratorService.findOneEntityByUserId(ctx.getActiveUserId());
    }
}
