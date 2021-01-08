/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.service;

import io.geekshop.common.utils.BeanMapper;
import io.geekshop.entity.ProductOptionEntity;
import io.geekshop.entity.ProductOptionGroupEntity;
import io.geekshop.mapper.ProductOptionEntityMapper;
import io.geekshop.mapper.ProductOptionGroupEntityMapper;
import io.geekshop.service.helpers.ServiceHelper;
import io.geekshop.types.product.CreateProductOptionInput;
import io.geekshop.types.product.UpdateProductOptionInput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Service
@RequiredArgsConstructor
public class ProductOptionService {
    private final ProductOptionEntityMapper productOptionEntityMapper;
    private final ProductOptionGroupEntityMapper productOptionGroupEntityMapper;

    public List<ProductOptionEntity> findAll() {
        return this.productOptionEntityMapper.selectList(null);
    }

    public ProductOptionEntity findOne(Long id) {
        return this.productOptionEntityMapper.selectById(id);
    }

    public ProductOptionEntity create(CreateProductOptionInput input) {
        ServiceHelper.getEntityOrThrow(
                productOptionGroupEntityMapper, ProductOptionGroupEntity.class, input.getProductOptionGroupId());
        ProductOptionEntity productOptionEntity = new ProductOptionEntity();
        productOptionEntity.setCode(input.getCode());
        productOptionEntity.setName(input.getName());
        productOptionEntity.setGroupId(input.getProductOptionGroupId());

        productOptionEntityMapper.insert(productOptionEntity);

        return productOptionEntity;
    }

    public ProductOptionEntity update(UpdateProductOptionInput input) {
        ProductOptionEntity productOptionEntity = ServiceHelper.getEntityOrThrow(
                productOptionEntityMapper, ProductOptionEntity.class, input.getId()
        );
        BeanMapper.patch(input, productOptionEntity);
        productOptionEntityMapper.updateById(productOptionEntity);
        return productOptionEntity;
    }
}
