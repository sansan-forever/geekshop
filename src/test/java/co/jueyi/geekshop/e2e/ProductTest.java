/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.e2e;

import co.jueyi.geekshop.*;
import co.jueyi.geekshop.config.TestConfig;
import co.jueyi.geekshop.types.product.ProductOptionGroup;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;

/**
 * Created on Dec, 2020 by @author bobo
 */
@GeekShopGraphQLTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
public class ProductTest {
    @Autowired
    TestHelper testHelper;

    @Autowired
    @Qualifier(TestConfig.ADMIN_CLIENT_BEAN)
    ApiClient adminClient;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockDataService mockDataService;

    @BeforeAll
    void beforeAll() throws IOException {
        PopulateOptions populateOptions = PopulateOptions.builder().customerCount(1).build();
        populateOptions.setInitialData(testHelper.getInitialData());
        populateOptions.setProductCsvPath(testHelper.getTestFixture("e2e-products-full.csv"));

        mockDataService.populate(populateOptions);
        adminClient.asSuperAdmin();
    }

    /**
     * products list query
     */

    @Test
    @Order(1)
    public void returns_all_products_when_no_options_passed() throws IOException {

    }
}
