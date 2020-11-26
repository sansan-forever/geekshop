/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.custom.security.Allow;
import co.jueyi.geekshop.entity.ProductEntity;
import co.jueyi.geekshop.entity.ProductVariantEntity;
import co.jueyi.geekshop.exception.UserInputException;
import co.jueyi.geekshop.service.ProductService;
import co.jueyi.geekshop.service.ProductVariantService;
import co.jueyi.geekshop.types.common.Permission;
import co.jueyi.geekshop.types.product.Product;
import co.jueyi.geekshop.types.product.ProductList;
import co.jueyi.geekshop.types.product.ProductListOptions;
import co.jueyi.geekshop.types.product.ProductVariant;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class ProductQuery implements GraphQLQueryResolver {

    private final ProductService productService;
    private final ProductVariantService productVariantService;

    @Allow(Permission.ReadCatalog)
    public ProductList adminProducts(ProductListOptions options, DataFetchingEnvironment dfe) {
        return this.productService.findAll(options);
    }

    /**
     * Get a Product either by id or slug. If neither id nor slug is specified, an error will result.
     */
    @Allow(Permission.ReadCatalog)
    public Product adminProduct(Long id, String slug, DataFetchingEnvironment dfe) {
        if (id != null) {
            ProductEntity productEntity = this.productService.findOne(id);
            if (productEntity == null) return null;
            if (slug != null && !slug.equals(productEntity.getSlug())) {
                throw new UserInputException("The provided id and slug refer to different Products");
            }
            return BeanMapper.map(productEntity, Product.class);
        } else if (slug != null) {
            ProductEntity productEntity = this.productService.findOneBySlug(slug);
            if (productEntity == null) return null;
            return BeanMapper.map(productEntity, Product.class);
        } else {
            throw new UserInputException("Either the Product id or slug must be provided");
        }
    }

    /**
     * Get a ProductVariant by id
     */
    @Allow(Permission.ReadCatalog)
    public ProductVariant productVariant(Long id, DataFetchingEnvironment dfe) {
        ProductVariantEntity productVariantEntity = productVariantService.findOne(id);
        if (productVariantEntity == null) return null;
        return BeanMapper.map(productVariantEntity, ProductVariant.class);
    }
}
