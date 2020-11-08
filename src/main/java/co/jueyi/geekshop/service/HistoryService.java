package co.jueyi.geekshop.service;

import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.entity.CustomerHistoryEntryEntity;
import co.jueyi.geekshop.exception.EntityNotFoundException;
import co.jueyi.geekshop.mapper.CustomerHistoryEntryEntityMapper;
import co.jueyi.geekshop.service.args.CreateCustomerHistoryEntryArgs;
import co.jueyi.geekshop.service.args.UpdateCustomerHistoryEntryArgs;
import co.jueyi.geekshop.service.helper.QueryHelper;
import co.jueyi.geekshop.service.helper.ServiceHelper;
import co.jueyi.geekshop.types.administrator.Administrator;
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
    private final CustomerHistoryEntryEntityMapper customerHistoryEntryMapper;

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
                this.customerHistoryEntryMapper.selectPage(page, queryWrapper);

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

    public HistoryEntry createHistoryEntryForCustomer(CreateCustomerHistoryEntryArgs args, Boolean isPublic) {
        Administrator administrator = this.getAdministratorFromContext(args.getCtx());
        CustomerHistoryEntryEntity customerHistoryEntryEntity =
                BeanMapper.map(args, CustomerHistoryEntryEntity.class);
        customerHistoryEntryEntity.setAdministratorId(administrator == null ? null : administrator.getId());
        customerHistoryEntryEntity.setPublic(BooleanUtils.toBoolean(isPublic));
        this.customerHistoryEntryMapper.insert(customerHistoryEntryEntity);
        return BeanMapper.map(customerHistoryEntryEntity, HistoryEntry.class);
    }

    public HistoryEntry updateCustomerHistoryEntry(UpdateCustomerHistoryEntryArgs args) {
        QueryWrapper<CustomerHistoryEntryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CustomerHistoryEntryEntity::getId, args.getEntryId())
                .eq(CustomerHistoryEntryEntity::getType, args.getType());
        CustomerHistoryEntryEntity customerHistoryEntryEntity =
                this.customerHistoryEntryMapper.selectOne(queryWrapper);
        if (customerHistoryEntryEntity == null) {
            throw new EntityNotFoundException("CustomerHistoryEntry", args.getEntryId());
        }

        if (args.getData() != null && !args.getData().isEmpty()) {
            customerHistoryEntryEntity.setData(args.getData());
        }
        Administrator administrator = this.getAdministratorFromContext(args.getCtx());
        if (administrator != null) {
            customerHistoryEntryEntity.setCustomerId(administrator.getId());
        }
        this.customerHistoryEntryMapper.updateById(customerHistoryEntryEntity);
        return BeanMapper.map(customerHistoryEntryEntity, HistoryEntry.class);
    }

    public void deleteCustomerHistoryEntry(Long id) {
        // 确保存在
        ServiceHelper.getEntityOrThrow(this.customerHistoryEntryMapper, id);
        this.customerHistoryEntryMapper.deleteById(id);
    }

    private Administrator getAdministratorFromContext(RequestContext ctx) {
        if (ctx.getActiveUserId() == null) return null;

        return this.administratorService.findOneByUserId(ctx.getActiveUserId());
    }
}
