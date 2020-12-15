/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.service;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.entity.FacetEntity;
import co.jueyi.geekshop.entity.FacetValueEntity;
import co.jueyi.geekshop.entity.ProductFacetValueJoinEntity;
import co.jueyi.geekshop.entity.ProductVariantFacetValueJoinEntity;
import co.jueyi.geekshop.mapper.FacetEntityMapper;
import co.jueyi.geekshop.mapper.FacetValueEntityMapper;
import co.jueyi.geekshop.mapper.ProductFacetValueJoinEntityMapper;
import co.jueyi.geekshop.mapper.ProductVariantFacetValueJoinEntityMapper;
import co.jueyi.geekshop.service.helpers.ServiceHelper;
import co.jueyi.geekshop.types.common.DeletionResponse;
import co.jueyi.geekshop.types.common.DeletionResult;
import co.jueyi.geekshop.types.facet.CreateFacetValueInput;
import co.jueyi.geekshop.types.facet.UpdateFacetValueInput;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Service
@RequiredArgsConstructor
public class FacetValueService {
    private final FacetValueEntityMapper facetValueEntityMapper;
    private final FacetEntityMapper facetEntityMapper;
    private final ProductFacetValueJoinEntityMapper productFacetValueJoinEntityMapper;
    private final ProductVariantFacetValueJoinEntityMapper productVariantFacetValueJoinEntityMapper;

    public List<FacetValueEntity> findAll() {
        return facetValueEntityMapper.selectList(null);
    }

    public FacetValueEntity findOne(Long id) {
        return facetValueEntityMapper.selectById(id);
    }

    public List<FacetValueEntity> findByIds(List<Long> ids) {
        return facetValueEntityMapper.selectBatchIds(ids);
    }

    public List<FacetValueEntity> findByFacetId(Long facetId) {
        QueryWrapper<FacetValueEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FacetValueEntity::getFacetId, facetId);
        return facetValueEntityMapper.selectList(queryWrapper);
    }

    public FacetValueEntity create(CreateFacetValueInput input) {
        // 确保Facet存在
        ServiceHelper.getEntityOrThrow(facetEntityMapper, FacetEntity.class, input.getFacetId());
        FacetValueEntity facetValueEntity = new FacetValueEntity();
        facetValueEntity.setFacetId(input.getFacetId());
        facetValueEntity.setCode(input.getCode());
        facetValueEntity.setName(input.getName());
        facetValueEntityMapper.insert(facetValueEntity);
        return facetValueEntity;
    }

    public FacetValueEntity update(UpdateFacetValueInput input) {
        FacetValueEntity facetValueEntity =
                ServiceHelper.getEntityOrThrow(facetValueEntityMapper, FacetValueEntity.class, input.getId());
        BeanMapper.patch(input, facetValueEntity);
        facetValueEntityMapper.updateById(facetValueEntity);
        return facetValueEntity;
    }

    public DeletionResponse delete(Long id) {
        FacetValueUsage facetValueUsage = this.checkFacetValueUsage(Arrays.asList(id));
        int productCount = facetValueUsage.getProductCount();
        int variantCount = facetValueUsage.getVariantCount();

        boolean isInUse = productCount > 0 || variantCount > 0;

        String message = "";
        DeletionResult result = null;

        if (!isInUse) {
            ServiceHelper.getEntityOrThrow(this.facetValueEntityMapper, FacetValueEntity.class, id);
            this.facetValueEntityMapper.deleteById(id);
            result = DeletionResult.DELETED;
        } else {
            message = "The selected FacetValue is assigned to " +
                    productCount + " Product(s) and " + variantCount + " Variant(s)";
            result = DeletionResult.NOT_DELETED;
        }

        DeletionResponse deletionResponse = new DeletionResponse();
        deletionResponse.setMessage(message);
        deletionResponse.setResult(result);

        return deletionResponse;
    }

    /**
     * Checks for usage of the given FacetValues in any Products or Variants, and returns the counts.
     */
    FacetValueUsage checkFacetValueUsage(List<Long> facetValueIds) {
        if (CollectionUtils.isEmpty(facetValueIds))
            return FacetValueUsage.builder().productCount(0).variantCount(0).build();

        QueryWrapper<ProductFacetValueJoinEntity> productFacetValueJoinEntityQueryWrapper = new QueryWrapper<>();
        productFacetValueJoinEntityQueryWrapper.lambda().
                in(ProductFacetValueJoinEntity::getFacetValueId, facetValueIds);
        List<ProductFacetValueJoinEntity> productFacetValueJoinEntities =
                this.productFacetValueJoinEntityMapper.selectList(productFacetValueJoinEntityQueryWrapper);
        int consumingProductsCount =
                productFacetValueJoinEntities.stream()
                        .map(ProductFacetValueJoinEntity::getProductId)
                        .collect(Collectors.toSet()).size();

        QueryWrapper<ProductVariantFacetValueJoinEntity> productVariantFacetValueJoinEntityQueryWrapper =
                new QueryWrapper<>();
        productVariantFacetValueJoinEntityQueryWrapper.lambda().
                in(ProductVariantFacetValueJoinEntity::getFacetValueId, facetValueIds);
        List<ProductVariantFacetValueJoinEntity> productVariantFacetValueJoinEntities =
                this.productVariantFacetValueJoinEntityMapper
                        .selectList(productVariantFacetValueJoinEntityQueryWrapper);
        int consumingVariantsCount =
                productVariantFacetValueJoinEntities.stream()
                        .map(ProductVariantFacetValueJoinEntity::getProductVariantId)
                        .collect(Collectors.toSet()).size();
        return FacetValueUsage.builder().productCount(consumingProductsCount).variantCount(consumingVariantsCount)
                .build();
    }

    @Data
    @Builder
    static class FacetValueUsage {
        private int productCount;
        private int variantCount;
    }

}
