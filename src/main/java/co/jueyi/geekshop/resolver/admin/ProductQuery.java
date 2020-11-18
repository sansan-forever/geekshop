/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.admin;

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

    public ProductList products(ProductListOptions options, DataFetchingEnvironment dfe) {
        return null; // TODO
    }

    /**
     * Get a Product either by id or slug. If neither id nor slug is specified, an error will result.
     */
    public Product product(Long id, String slug, DataFetchingEnvironment dfe) {
        return null; // TODO
    }

    /**
     * Get a ProductVariant by id
     */
    public ProductVariant productVariant(Long id, DataFetchingEnvironment dfe) {
        return null; // TODO
    }
}
