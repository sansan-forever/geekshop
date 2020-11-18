/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.types.common.DeletionResponse;
import co.jueyi.geekshop.types.product.*;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class ProductMutation implements GraphQLMutationResolver {
    /**
     * Create a new Product
     */
    public Product createProduct(CreateProductInput input, DataFetchingEnvironment dfe) {
        return null; // TODO
    }

    /**
     * Update an existing Product
     */
    public Product updateProduct(UpdateProductInput input, DataFetchingEnvironment dfe) {
        return null; // TODO
    }

    /**
     * Delete a Product
     */
    public DeletionResponse deleteProduct(Long id, DataFetchingEnvironment dfe) {
        return null; // TODO
    }

    /**
     * Add an OptionGroup to a Product
     */
    public Product addOptionGroupToProduct(Long productId, Long optionGroupId, DataFetchingEnvironment dfe) {
        return null; // TODO
    }

    /**
     * Remove an OptionGroup from a Product
     */
    public Product removeOptionGroupFromProduct(Long productId, Long optionGroupId, DataFetchingEnvironment dfe) {
        return null; // TODO
    }

    /**
     * Create a set of ProductVariants based on the OptionGroups assigned to the given Product
     */
    public List<ProductVariant> createProductVariants(
            List<CreateProductVariantInput> input, DataFetchingEnvironment dfe) {
        return null; // TODO
    }

    /**
     * Update exisiting ProductVariants
     */
    public List<ProductVariant> updateProductVariants(
            List<UpdateProductVariantInput> input, DataFetchingEnvironment dfe) {
        return null; // TODO
    }

    /**
     * Delete a ProductVariant
     */
    public DeletionResponse deleteProductVariant(Long id, DataFetchingEnvironment dfe) {
        return null; // TODO
    }
}
