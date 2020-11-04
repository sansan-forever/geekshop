package co.jueyi.geekshop.config;

import co.jueyi.geekshop.custom.scalar.DateTimeScalar;
import co.jueyi.geekshop.types.administrator.AdministratorList;
import co.jueyi.geekshop.types.asset.AssetList;
import co.jueyi.geekshop.types.collection.CollectionList;
import co.jueyi.geekshop.types.facet.FacetList;
import co.jueyi.geekshop.types.payment.PaymentMethod;
import co.jueyi.geekshop.types.product.ProductList;
import co.jueyi.geekshop.types.promotion.PromotionList;
import co.jueyi.geekshop.types.shipping.ShippingMethodList;
import co.jueyi.geekshop.types.stock.*;
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

    // custom mapping
    @Bean
    public SchemaParserDictionary schemaParserDictionary() {
        SchemaParserDictionary schemaParserDictionary = new SchemaParserDictionary();
        schemaParserDictionary.add(PaymentMethod.class);
//        schemaParserDictionary.add(StockMovement.class);
        schemaParserDictionary.add(StockAdjustment.class);
        schemaParserDictionary.add(Sale.class);
        schemaParserDictionary.add(Cancellation.class);
        schemaParserDictionary.add(Return.class);
        schemaParserDictionary.add(AdministratorList.class);
        schemaParserDictionary.add(AssetList.class);
        schemaParserDictionary.add(CollectionList.class);
        schemaParserDictionary.add(FacetList.class);
        schemaParserDictionary.add(ProductList.class);
        schemaParserDictionary.add(PromotionList.class);
        schemaParserDictionary.add(ShippingMethodList.class);
        return schemaParserDictionary;
    }
}
