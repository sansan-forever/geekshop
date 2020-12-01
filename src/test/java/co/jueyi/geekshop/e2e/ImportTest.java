/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.e2e;

import co.jueyi.geekshop.*;
import co.jueyi.geekshop.config.TestConfig;
import co.jueyi.geekshop.types.ImportInfo;
import co.jueyi.geekshop.types.asset.Asset;
import co.jueyi.geekshop.types.product.Product;
import co.jueyi.geekshop.types.product.ProductList;
import co.jueyi.geekshop.types.product.ProductOptionGroup;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graphql.spring.boot.test.GraphQLResponse;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.util.Objects;

/**
 * Created on Nov, 2020 by @author bobo
 */
@GeekShopGraphQLTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
public class ImportTest {

    static final String IMPORT_PRODUCTS = "graphql/import/import_products.graphqls";
    static final String GET_PRODUCTS = "graphql/import/get_products.graphqls";

    @Autowired
    @Qualifier(TestConfig.ADMIN_CLIENT_BEAN)
    ApiClient adminClient;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockDataService mockDataService;

    @BeforeAll
    void beforeAll() throws IOException {
        PopulateOptions populateOptions = PopulateOptions.builder().customerCount(0).build();
        populateOptions.setInitialData(TestHelper.getInitialData());
        populateOptions.setProductCsvPath(TestHelper.getTestFixture("e2e-products-empty.csv"));

        mockDataService.populate(populateOptions);
        adminClient.asSuperAdmin();
    }

    @Test
    public void import_products() throws IOException {
        GraphQLResponse graphQLResponse = adminClient.uploadSingleFile(
                IMPORT_PRODUCTS,
                "src/test/resources/fixtures",
                "product-import.csv",
                "text/csv"
        );
        assertThat(graphQLResponse.isOk());

        ImportInfo importInfo = graphQLResponse.get("$.data.importProducts", ImportInfo.class);
        assertThat(importInfo.getImported()).isEqualTo(4);
        assertThat(importInfo.getProcessed()).isEqualTo(4);

        graphQLResponse = adminClient.perform(GET_PRODUCTS, null);
        assertThat(graphQLResponse.isOk());
        ProductList productResult = graphQLResponse.get("$.data.adminProducts", ProductList.class);
        assertThat(productResult.getTotalItems()).isEqualTo(4);

        Product paperStretcher = productResult.getItems().stream()
                .filter(p -> Objects.equals(p.getName(), "Perfect Paper Stretcher")).findFirst().get();
        verifyPaperStretcher(paperStretcher);
        Product easel = productResult.getItems().stream()
                .filter(p -> Objects.equals(p.getName(), "Mabef M/02 Studio Easel")).findFirst().get();
        Product pencils = productResult.getItems().stream()
                .filter(p -> Objects.equals(p.getName(), "Giotto Mega Pencils")).findFirst().get();
        Product smock = productResult.getItems().stream()
                .filter(p -> Objects.equals(p.getName(), "Artists Smock")).findFirst().get();
    }

    private void verifyPaperStretcher(Product product) {
        assertThat(product.getAssets()).hasSize(2);
        Asset asset1 = product.getAssets().get(0);
        assertThat(asset1.getId()).isEqualTo(1L);
        assertThat(asset1.getName()).isEqualTo("pps1.jpg");
        assertThat(asset1.getPreview()).isEqualTo("target/test-assets/pps1__preview.jpg");
        assertThat(asset1.getSource()).isEqualTo("target/test-assets/pps1.jpg");
        Asset asset2 = product.getAssets().get(1);
        assertThat(asset2.getId()).isEqualTo(2L);
        assertThat(asset2.getName()).isEqualTo("pps2.jpg");
        assertThat(asset2.getPreview()).isEqualTo("target/test-assets/pps2__preview.jpg");
        assertThat(asset2.getSource()).isEqualTo("target/test-assets/pps2.jpg");

        assertThat(product.getDescription()).isEqualTo("A great device for stretching paper.");
        Asset featuredAsset = product.getFeaturedAsset();
        assertThat(featuredAsset.getId()).isEqualTo(1L);
        assertThat(featuredAsset.getName()).isEqualTo("pps1.jpg");
        assertThat(featuredAsset.getPreview()).isEqualTo("target/test-assets/pps1__preview.jpg");
        assertThat(featuredAsset.getSource()).isEqualTo("target/test-assets/pps1.jpg");

        assertThat(product.getId()).isEqualTo(1L);
        assertThat(product.getName()).isEqualTo("Perfect Paper Stretcher");

        assertThat(product.getOptionGroups()).hasSize(1);
        ProductOptionGroup optionGroup = product.getOptionGroups().get(0);
        assertThat(optionGroup.getCode()).isEqualTo("perfect-paper-stretcher-size");
        assertThat(optionGroup.getId()).isEqualTo(1L);
        assertThat(optionGroup.getName()).isEqualTo("size");

        assertThat(product.getSlug()).isEqualTo("perfect-paper-stretcher");

        assertThat(product.getVariants()).hasSize(3);

        // TODO verify variants
        // 2020.12.1

    }

}
