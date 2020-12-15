/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.service;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.entity.ProductOptionEntity;
import co.jueyi.geekshop.entity.ProductOptionGroupEntity;
import co.jueyi.geekshop.mapper.ProductOptionEntityMapper;
import co.jueyi.geekshop.mapper.ProductOptionGroupEntityMapper;
import co.jueyi.geekshop.service.helpers.ServiceHelper;
import co.jueyi.geekshop.types.product.CreateProductOptionInput;
import co.jueyi.geekshop.types.product.UpdateProductOptionInput;
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
