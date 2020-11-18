/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.custom.security.Allow;
import co.jueyi.geekshop.entity.ProductOptionEntity;
import co.jueyi.geekshop.entity.ProductOptionGroupEntity;
import co.jueyi.geekshop.service.ProductOptionGroupService;
import co.jueyi.geekshop.service.ProductOptionService;
import co.jueyi.geekshop.types.common.Permission;
import co.jueyi.geekshop.types.product.*;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class ProductOptionMutation implements GraphQLMutationResolver {

    private final ProductOptionGroupService productOptionGroupService;
    private final ProductOptionService productOptionService;

    /**
     * Craete a new ProductOptionGroup
     */
    @Allow(Permission.CreateCatalog)
    public ProductOptionGroup createProductOptionGroup(
            CreateProductOptionGroupInput input, DataFetchingEnvironment dfe) {
        ProductOptionGroupEntity productOptionGroupEntity = this.productOptionGroupService.create(input);
        return BeanMapper.map(productOptionGroupEntity, ProductOptionGroup.class);
    }

    /**
     * Update an existing ProductOptionGroup
     */
    @Allow(Permission.UpdateCatalog)
    public ProductOptionGroup updateProductOptionGroup(
            UpdateProductOptionGroupInput input, DataFetchingEnvironment dfe) {
        ProductOptionGroupEntity productOptionGroupEntity = this.productOptionGroupService.update(input);
        return BeanMapper.map(productOptionGroupEntity, ProductOptionGroup.class);
    }

    /**
     * Create a new ProductOption within a ProductOptionGroup
     */
    @Allow(Permission.CreateCatalog)
    public ProductOption createProductOption(CreateProductOptionInput input, DataFetchingEnvironment dfe) {
        ProductOptionEntity productOptionEntity = this.productOptionService.create(input);
        return BeanMapper.map(productOptionEntity, ProductOption.class);
    }

    /**
     * Update a new ProductOption within a ProductOptionGroup
     */
    @Allow(Permission.UpdateCatalog)
    public ProductOption updateProductOption(UpdateProductOptionInput input, DataFetchingEnvironment dfe) {
        ProductOptionEntity productOptionEntity = this.productOptionService.update(input);
        return BeanMapper.map(productOptionEntity, ProductOption.class);
    }
}
