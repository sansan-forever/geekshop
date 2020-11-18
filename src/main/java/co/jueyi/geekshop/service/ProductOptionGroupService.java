/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.service;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.entity.ProductOptionGroupEntity;
import co.jueyi.geekshop.mapper.ProductOptionGroupEntityMapper;
import co.jueyi.geekshop.service.helper.ServiceHelper;
import co.jueyi.geekshop.types.product.CreateProductOptionGroupInput;
import co.jueyi.geekshop.types.product.CreateProductOptionInput;
import co.jueyi.geekshop.types.product.UpdateProductOptionGroupInput;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Service
@RequiredArgsConstructor
public class ProductOptionGroupService {
    private final ProductOptionGroupEntityMapper productOptionGroupEntityMapper;
    private final ProductOptionService productOptionService;

    public List<ProductOptionGroupEntity> findAll(String filterTerm) {
        QueryWrapper<ProductOptionGroupEntity> queryWrapper = new QueryWrapper<>();
        if (filterTerm != null) {
            queryWrapper.lambda().like(ProductOptionGroupEntity::getCode, filterTerm);
        }
        List<ProductOptionGroupEntity> productOptionGroupEntityList =
                productOptionGroupEntityMapper.selectList(queryWrapper);
        return productOptionGroupEntityList;
    }

    public ProductOptionGroupEntity findOne(Long id) {
        return this.productOptionGroupEntityMapper.selectById(id);
    }

    public List<ProductOptionGroupEntity> getOptionGroupsByProductId(Long productId) {
        QueryWrapper<ProductOptionGroupEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ProductOptionGroupEntity::getProductId, productId)
                .orderByAsc(ProductOptionGroupEntity::getId);
        return this.productOptionGroupEntityMapper.selectList(queryWrapper);
    }

    @Transactional
    public ProductOptionGroupEntity create(CreateProductOptionGroupInput input) {
        ProductOptionGroupEntity productOptionGroupEntity = new ProductOptionGroupEntity();
        productOptionGroupEntity.setCode(input.getCode());
        productOptionGroupEntity.setName(input.getName());
        this.productOptionGroupEntityMapper.insert(productOptionGroupEntity);
        if (!CollectionUtils.isEmpty(input.getOptions())) {
            input.getOptions().forEach(createGroupOptionInput -> {
                CreateProductOptionInput createProductOptionInput =
                        BeanMapper.map(createGroupOptionInput, CreateProductOptionInput.class);
                createProductOptionInput.setProductOptionGroupId(productOptionGroupEntity.getProductId());
                productOptionService.create(createProductOptionInput);
            });
        }
        return productOptionGroupEntity;
    }

    public ProductOptionGroupEntity update(UpdateProductOptionGroupInput input) {
        ProductOptionGroupEntity productOptionGroupEntity =
                ServiceHelper.getEntityOrThrow(
                        productOptionGroupEntityMapper, ProductOptionGroupEntity.class, input.getId());
        BeanMapper.patch(input, productOptionGroupEntity);
        productOptionGroupEntityMapper.updateById(productOptionGroupEntity);
        return productOptionGroupEntity;
    }
}