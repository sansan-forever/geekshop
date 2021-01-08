/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.config;

import io.geekshop.custom.scalar.DateTimeScalar;
import io.geekshop.types.administrator.AdministratorList;
import io.geekshop.types.asset.AssetList;
import io.geekshop.types.collection.CollectionList;
import io.geekshop.types.facet.FacetList;
import io.geekshop.types.payment.PaymentMethod;
import io.geekshop.types.product.ProductList;
import io.geekshop.types.promotion.PromotionList;
import io.geekshop.types.shipping.ShippingMethodList;
import io.geekshop.types.stock.*;
import graphql.kickstart.servlet.apollo.ApolloScalars;
import graphql.kickstart.tools.SchemaParserDictionary;
import graphql.scalars.ExtendedScalars;
import graphql.schema.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Configuration
public class GraphQLConfig {
    @Bean
    public GraphQLScalarType dateTime() {
        return DateTimeScalar.build();
    }

    @Bean
    public GraphQLScalarType upload() {
        return ApolloScalars.Upload;
    }

    @Bean
    public GraphQLScalarType json() {
        return ExtendedScalars.Json;
    }
}
