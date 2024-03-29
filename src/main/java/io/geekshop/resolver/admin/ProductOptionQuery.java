/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.resolver.admin;

import io.geekshop.common.utils.BeanMapper;
import io.geekshop.custom.security.Allow;
import io.geekshop.entity.ProductOptionGroupEntity;
import io.geekshop.service.ProductOptionGroupService;
import io.geekshop.types.common.Permission;
import io.geekshop.types.product.ProductOptionGroup;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class ProductOptionQuery implements GraphQLQueryResolver {
    private final ProductOptionGroupService productOptionGroupService;

    @Allow(Permission.ReadCatalog)
    public List<ProductOptionGroup> productOptionGroups(String filterTerm, DataFetchingEnvironment dfe) {
        List<ProductOptionGroupEntity> productOptionGroupEntityList = productOptionGroupService.findAll(filterTerm);
        return productOptionGroupEntityList.stream()
                .map(productOptionGroupEntity -> BeanMapper.map(productOptionGroupEntity, ProductOptionGroup.class))
                .collect(Collectors.toList());
    }

    @Allow(Permission.ReadCatalog)
    public ProductOptionGroup productOptionGroup(Long id, DataFetchingEnvironment dfe) {
        ProductOptionGroupEntity productOptionGroupEntity = this.productOptionGroupService.findOne(id);
        if (productOptionGroupEntity == null) return null;
        return BeanMapper.map(productOptionGroupEntity, ProductOptionGroup.class);
    }
}
