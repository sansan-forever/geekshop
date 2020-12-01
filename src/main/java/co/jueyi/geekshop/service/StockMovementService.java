/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.service;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.entity.ProductVariantEntity;
import co.jueyi.geekshop.entity.StockMovementEntity;
import co.jueyi.geekshop.exception.InternalServerError;
import co.jueyi.geekshop.mapper.ProductVariantEntityMapper;
import co.jueyi.geekshop.mapper.StockMovementEntityMapper;
import co.jueyi.geekshop.service.helper.PageInfo;
import co.jueyi.geekshop.service.helper.ServiceHelper;
import co.jueyi.geekshop.types.order.Order;
import co.jueyi.geekshop.types.order.OrderItem;
import co.jueyi.geekshop.types.order.OrderLine;
import co.jueyi.geekshop.types.product.StockMovementListOptions;
import co.jueyi.geekshop.types.stock.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings("Duplicates")
public class StockMovementService {
    private final StockMovementEntityMapper stockMovementEntityMapper;
    private final ProductVariantEntityMapper productVariantEntityMapper;

    public StockMovementList getStockMovementsByProductVaraintId(
            Long productVariantId, StockMovementListOptions options) {
        PageInfo pageInfo = ServiceHelper.getListOptions(options);
        IPage<StockMovementEntity> page = new Page<>(pageInfo.current, pageInfo.size);
        QueryWrapper<StockMovementEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(StockMovementEntity::getProductVariantId, productVariantId);
        if (options != null && options.getType() != null) {
            queryWrapper.lambda().eq(StockMovementEntity::getType, options.getType());
        }
        IPage<StockMovementEntity> stockMovementEntityPage =
                this.stockMovementEntityMapper.selectPage(page, queryWrapper);

        StockMovementList stockMovementList = new StockMovementList();
        stockMovementList.setTotalItems((int) stockMovementEntityPage.getTotal()); // 设置满足条件总记录数

        if (CollectionUtils.isEmpty(stockMovementEntityPage.getRecords()))
            return stockMovementList; // 返回空

        // 将持久化实体类型转换成GraphQL传输类型
        stockMovementEntityPage.getRecords().forEach(stockMovementEntity -> {
            StockMovement stockMovement = BeanMapper.map(stockMovementEntity, StockMovement.class);
            stockMovementList.getItems().add(stockMovement);
        });

        return stockMovementList;
    }

    public StockMovement adjustProductVariantStock(
            Long productVariantId, Integer oldStockLevel, Integer newStockLevel) {
        if (oldStockLevel.equals(newStockLevel)) {
            return null;
        }

        Integer delta = newStockLevel - oldStockLevel;

        StockMovementEntity adjustment = new StockMovementEntity();
        adjustment.setType(StockMovementType.ADJUSTMENT);
        adjustment.setQuantity(delta);
        adjustment.setProductVariantId(productVariantId);

        this.stockMovementEntityMapper.insert(adjustment);

        return BeanMapper.map(adjustment, StockMovement.class);
    }

    @Transactional
    public List<StockMovement> createSalesForOrder(Order order) { // TODO OrderEntity?
        if (BooleanUtils.isTrue(order.getActive())) {
            throw new InternalServerError("Cannot create a Sale for an Order which is still active");
        }

        List<StockMovement> saleList = new ArrayList<>();
        for(OrderLine line : order.getLines()) {
            StockMovementEntity saleEntity = new StockMovementEntity();
            saleEntity.setType(StockMovementType.SALE);
            saleEntity.setQuantity(line.getQuantity() * -1);
            saleEntity.setProductVariantId(line.getProductVariantId());
            saleEntity.setOrderLineId(line.getId());

            this.stockMovementEntityMapper.insert(saleEntity);

            ProductVariantEntity productVariantEntity =
                    this.productVariantEntityMapper.selectById(line.getProductVariantId());
            if (productVariantEntity.isTrackInventory()) {
                Integer stockOnHand = productVariantEntity.getStockOnHand() - line.getQuantity();
                productVariantEntity.setStockOnHand(stockOnHand);
                this.productVariantEntityMapper.updateById(productVariantEntity);
            }

            StockMovement sale = BeanMapper.map(saleEntity, StockMovement.class);
            saleList.add(sale);
        }

        return saleList;
    }

    public List<StockMovement> createCancellationsForOrderItems(List<OrderItem> items) { // TODO OrderItemEntity?
        return null; // TODO
    }
}
