/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.data_import;

import co.jueyi.geekshop.data_import.parser.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Slf4j
@RunWith(SpringRunner.class)
public class ImportParserTest {

    public static final String TEST_FIXTURES_PATH = "test_fixtures/";

    @Test
    public void single_product_with_a_single_variant() throws IOException {
        ImportParser importParser = new ImportParser();

        Reader reader = loadTestFixture("single-product-single-variant.csv");
        ParseResult<ParsedProductWithVariants> parseResult = importParser.parseProducts(reader);
        assertThat(parseResult.getErrors()).isEmpty();
        assertThat(parseResult.getProcessed()).isEqualTo(1L);
        assertThat(parseResult.getResults()).hasSize(1);
        ParsedProduct product = parseResult.getResults().get(0).getProduct();
        assertThat(product.getAssetPaths()).containsExactlyInAnyOrder("pps1.jpg", "pps2.jpg");
        assertThat(product.getDescription()).isEqualTo("A great device for stretching paper.");
        assertThat(product.getFacets()).containsExactlyInAnyOrder(
                new StringFacet("brand", "KB"),
                new StringFacet("type", "Accessory")
        );
        assertThat(product.getName()).isEqualTo("Perfect Paper Stretcher");
        assertThat(product.getOptionGroups()).isEmpty();
        assertThat(product.getSlug()).isEqualTo("perfect-paper-stretcher");

        assertThat(parseResult.getResults().get(0).getVariants()).hasSize(1);
        ParsedProductVariant variant = parseResult.getResults().get(0).getVariants().get(0);
        assertThat(variant.getAssetPaths()).isEmpty();
        assertThat(variant.getFacets()).containsExactlyInAnyOrder(
                new StringFacet("material", "Wood")
        );
        assertThat(variant.getOptionValues()).isEmpty();
        assertThat(variant.getPrice()).isEqualTo(45.3F);
        assertThat(variant.getSku()).isEqualTo("PPS12");
        assertThat(variant.getStockOnHand()).isEqualTo(10);
        assertThat(variant.getTrackInventory()).isEqualTo(false);
    }


    private Reader loadTestFixture(String fileName) throws IOException {
        Resource resource =
                new DefaultResourceLoader().getResource("classpath:" + TEST_FIXTURES_PATH + fileName);
        Reader reader = Files.newBufferedReader(Paths.get(resource.getURI()));
        return reader;
    }
}
