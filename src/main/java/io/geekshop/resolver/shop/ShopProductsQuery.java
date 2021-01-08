/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.resolver.shop;

import io.geekshop.common.utils.BeanMapper;
import io.geekshop.custom.security.Allow;
import io.geekshop.entity.CollectionEntity;
import io.geekshop.entity.ProductEntity;
import io.geekshop.exception.UserInputException;
import io.geekshop.service.CollectionService;
import io.geekshop.service.ProductService;
import io.geekshop.service.SearchService;
import io.geekshop.types.collection.Collection;
import io.geekshop.types.collection.CollectionFilterParameter;
import io.geekshop.types.collection.CollectionList;
import io.geekshop.types.collection.CollectionListOptions;
import io.geekshop.types.common.BooleanOperators;
import io.geekshop.types.common.Permission;
import io.geekshop.types.common.SearchInput;
import io.geekshop.types.product.*;
import io.geekshop.types.search.SearchResponse;
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
public class ShopProductsQuery implements GraphQLQueryResolver {
    private final ProductService productService;
    private final CollectionService collectionService;
    private final SearchService searchService;

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
        booleanOperators.setEq(false);
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

    @Allow(Permission.Public)
    public SearchResponse search(SearchInput input, DataFetchingEnvironment dfe) {
        SearchResponse searchResponse = this.searchService.search(input, true);
        // ensure the facetValues resolver has access to the input
        searchResponse.setSearchInput(input);
        return searchResponse;
    }
}
