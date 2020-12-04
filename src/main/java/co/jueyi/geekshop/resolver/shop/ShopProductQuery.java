/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.shop;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.entity.CollectionEntity;
import co.jueyi.geekshop.entity.ProductEntity;
import co.jueyi.geekshop.exception.UserInputException;
import co.jueyi.geekshop.service.CollectionService;
import co.jueyi.geekshop.service.ProductService;
import co.jueyi.geekshop.types.collection.Collection;
import co.jueyi.geekshop.types.collection.CollectionFilterParameter;
import co.jueyi.geekshop.types.collection.CollectionList;
import co.jueyi.geekshop.types.collection.CollectionListOptions;
import co.jueyi.geekshop.types.common.BooleanOperators;
import co.jueyi.geekshop.types.product.*;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class ShopProductQuery implements GraphQLQueryResolver {
    private final ProductService productService;
    private final CollectionService collectionService;

    public ProductList products(ProductListOptions options, DataFetchingEnvironment dfe) {

        if (options == null) {
            options = new ProductListOptions();
        }
        if (options.getFilter() == null) {
            options.setFilter(new ProductFilterParameter());
        }
        ProductFilterParameter filter = options.getFilter();
        BooleanOperators booleanOperators = new BooleanOperators();
        booleanOperators.setEq(true);
        filter.setEnabled(booleanOperators);

        return this.productService.findAll(options);
    }

    public Product product(Long id, String slug, DataFetchingEnvironment dfe) {
        ProductEntity productEntity = null;
        if (id != null) {
            productEntity = this.productService.findOne(id);
        } else if (slug != null) {
            productEntity = this.productService.findOneBySlug(slug);
        } else {
            throw new UserInputException("Either the Product id or slug must be provided");
        }
        if (productEntity == null) return null;
        if (!productEntity.isEnabled()) return null;
        return BeanMapper.map(productEntity, Product.class);
    }

    public CollectionList collections(CollectionListOptions options, DataFetchingEnvironment dfe) {
        if (options == null) {
            options = new CollectionListOptions();
        }
        if (options.getFilter() == null) {
            options.setFilter(new CollectionFilterParameter());
        }
        CollectionFilterParameter filter = options.getFilter();
        BooleanOperators booleanOperators = new BooleanOperators();
        booleanOperators.setEq(true);
        filter.setPrivateOnly(booleanOperators);

        return this.collectionService.findAll(options);
    }

    public Collection collection(Long id, String slug, DataFetchingEnvironment dfe) {
        CollectionEntity collectionEntity = null;
        if (id != null) {
            collectionEntity = this.collectionService.findOne(id);
            if (slug != null && collectionEntity != null && !Objects.equals(collectionEntity.getSlug(), slug)) {
                throw new UserInputException("The provided id and slug refer to different Collections");
            }
        } else if (slug != null) {
            collectionEntity = this.collectionService.findOneBySlug(slug);
        } else {
            throw new UserInputException("Either the Collection id or slug must be provided");
        }
        if (collectionEntity == null) return null;
        if (collectionEntity.isPrivateOnly()) return null;
        return BeanMapper.map(collectionEntity, Collection.class);
    }
}
