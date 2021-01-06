/*
 * Copyright (c) 2021 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.e2e;

import co.jueyi.geekshop.ApiClient;
import co.jueyi.geekshop.GeekShopGraphQLTest;
import co.jueyi.geekshop.MockDataService;
import co.jueyi.geekshop.PopulateOptions;
import co.jueyi.geekshop.config.TestConfig;
import co.jueyi.geekshop.types.common.LogicalOperator;
import co.jueyi.geekshop.types.common.SearchInput;
import co.jueyi.geekshop.types.common.SearchResultSortParameter;
import co.jueyi.geekshop.types.common.SortOrder;
import co.jueyi.geekshop.types.facet.CreateFacetInput;
import co.jueyi.geekshop.types.facet.CreateFacetValueWithFacetInput;
import co.jueyi.geekshop.types.facet.Facet;
import co.jueyi.geekshop.types.product.UpdateProductInput;
import co.jueyi.geekshop.types.product.UpdateProductVariantInput;
import co.jueyi.geekshop.types.search.FacetValueResult;
import co.jueyi.geekshop.types.search.SearchResponse;
import co.jueyi.geekshop.types.search.SearchResult;
import co.jueyi.geekshop.utils.TestHelper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.GraphQLResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on Jan, 2021 by @author bobo
 */
@GeekShopGraphQLTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
public class ProductSearchTest {

    static final String SHARED_GRAPHQL_RESOURCE_TEMPLATE = "graphql/shared/%s.graphqls";
    static final String CREATE_FACET =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "create_facet");
    static final String FACET_WITH_VALUES_FRAGMENT =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "facet_with_values_fragment");
    static final String FACET_VALUE_FRAGMENT =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "facet_value_fragment");
    static final String UPDATE_PRODUCT_VARIANTS =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "update_product_variants");

    static final String ASSET_FRAGMENT =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "asset_fragment");
    static final String PRODUCT_VARIANT_FRAGMENT =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "product_variant_fragment");
    static final String PRODUCT_WITH_VARIANTS_FRAGMENT =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "product_with_variants_fragment");
    static final String UPDATE_PRODUCT =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "update_product");

    static final String SHOP_SEARCH_GRAPHQL_RESOURCE_TEMPLATE = "graphql/shop/search/%s.graphqls";
    static final String SEARCH_PRODUCTS_SHOP =
            String.format(SHOP_SEARCH_GRAPHQL_RESOURCE_TEMPLATE, "search_products_shop");
    static final String SEARCH_GET_FACET_VALUES =
            String.format(SHOP_SEARCH_GRAPHQL_RESOURCE_TEMPLATE, "search_get_facet_values");

    @Autowired
    TestHelper testHelper;

    @Autowired
    @Qualifier(TestConfig.ADMIN_CLIENT_BEAN)
    ApiClient adminClient;

    @Autowired
    @Qualifier(TestConfig.SHOP_CLIENT_BEAN)
    ApiClient shopClient;

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

    void testTotalItems(ApiClient client) throws IOException {
        SearchInput input = new SearchInput();

        JsonNode inputNode = objectMapper.valueToTree(input);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        GraphQLResponse graphQLResponse = client.perform(SEARCH_PRODUCTS_SHOP, variables);
        SearchResponse searchResponse = graphQLResponse.get("$.data.search", SearchResponse.class);

        assertThat(searchResponse.getTotalItems()).isEqualTo(34);
    }

    void testMatchSearchTerm(ApiClient client) throws IOException {
        SearchInput input = new SearchInput();
        input.setTerm("camera");
        SearchResultSortParameter sortParameter = new SearchResultSortParameter();
        sortParameter.setName(SortOrder.ASC);

        JsonNode inputNode = objectMapper.valueToTree(input);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        GraphQLResponse graphQLResponse = client.perform(SEARCH_PRODUCTS_SHOP, variables);
        SearchResponse searchResponse = graphQLResponse.get("$.data.search", SearchResponse.class);

        assertThat(searchResponse.getItems().stream().map(i -> i.getProductName()).collect(Collectors.toList()))
                .containsExactlyInAnyOrder("Camera Lens", "Instant Camera", "Slr Camera");
    }

    void testMatchFacetIdsAnd(ApiClient client) throws IOException {
        SearchInput input = new SearchInput();
        input.getFacetValueIds().addAll(Arrays.asList(1L, 2L));
        input.setFacetValueOperator(LogicalOperator.AND);

        JsonNode inputNode = objectMapper.valueToTree(input);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        GraphQLResponse graphQLResponse = client.perform(SEARCH_PRODUCTS_SHOP, variables);
        SearchResponse searchResponse = graphQLResponse.get("$.data.search", SearchResponse.class);

        assertThat(searchResponse.getItems().stream().map(i -> i.getProductName()).collect(Collectors.toSet()))
                .containsExactlyInAnyOrder(
                        "Laptop",
                        "Curvy Monitor",
                        "Gaming PC",
                        "Hard Drive",
                        "Clacky Keyboard",
                        "USB Cable");
    }

    void testMatchFacetIdsOr(ApiClient client) throws IOException {
        SearchInput input = new SearchInput();
        input.getFacetValueIds().addAll(Arrays.asList(1L, 5L));
        input.setPageSize(100);
        input.setFacetValueOperator(LogicalOperator.OR);

        JsonNode inputNode = objectMapper.valueToTree(input);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        GraphQLResponse graphQLResponse = client.perform(SEARCH_PRODUCTS_SHOP, variables);
        SearchResponse searchResponse = graphQLResponse.get("$.data.search", SearchResponse.class);

        assertThat(searchResponse.getItems().stream().map(i -> i.getProductName()).collect(Collectors.toSet()))
                .containsExactlyInAnyOrder(
                        "Laptop",
                        "Curvy Monitor",
                        "Gaming PC",
                        "Hard Drive",
                        "Clacky Keyboard",
                        "USB Cable",
                        "Instant Camera",
                        "Camera Lens",
                        "Tripod",
                        "Slr Camera",
                        "Spiky Cactus",
                        "Orchid",
                        "Bonsai Tree"
                );
    }


    void testMatchCollectionId(ApiClient client) throws IOException {
        SearchInput input = new SearchInput();
        input.setCollectionId(2L);
        input.setPageSize(100);

        JsonNode inputNode = objectMapper.valueToTree(input);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        GraphQLResponse graphQLResponse = client.perform(SEARCH_PRODUCTS_SHOP, variables);
        SearchResponse searchResponse = graphQLResponse.get("$.data.search", SearchResponse.class);

        assertThat(searchResponse.getItems().stream().map(i -> i.getProductName()).collect(Collectors.toSet()))
                .containsExactlyInAnyOrder(
                        "Spiky Cactus",
                        "Orchid",
                        "Bonsai Tree"
                );
    }

    void testMatchCollectionSlug(ApiClient client) throws IOException {
        SearchInput input = new SearchInput();
        input.setCollectionSlug("plants");
        input.setPageSize(100);

        JsonNode inputNode = objectMapper.valueToTree(input);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        GraphQLResponse graphQLResponse = client.perform(SEARCH_PRODUCTS_SHOP, variables);
        SearchResponse searchResponse = graphQLResponse.get("$.data.search", SearchResponse.class);

        assertThat(searchResponse.getItems().stream().map(i -> i.getProductName()).collect(Collectors.toSet()))
                .containsExactlyInAnyOrder(
                        "Spiky Cactus",
                        "Orchid",
                        "Bonsai Tree"
                );
    }

    void testPrices(ApiClient client) throws IOException {
        SearchInput input = new SearchInput();
        input.setPageSize(3);

        JsonNode inputNode = objectMapper.valueToTree(input);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        GraphQLResponse graphQLResponse = client.perform(SEARCH_PRODUCTS_SHOP, variables);
        SearchResponse searchResponse = graphQLResponse.get("$.data.search", SearchResponse.class);

        assertThat(searchResponse.getItems().stream().map(i -> i.getPrice()).collect(Collectors.toSet()))
                .containsExactlyInAnyOrder(
                        129900, 139900, 219900
                );
    }

    void testSorting(ApiClient client, String sortBy) throws IOException {
        SearchInput input = new SearchInput();
        SearchResultSortParameter sortParameter = new SearchResultSortParameter();
        if ("name".equals(sortBy)) {
            sortParameter.setName(SortOrder.DESC);
        } else {
            sortParameter.setPrice(SortOrder.DESC);
        }
        input.setSort(sortParameter);
        input.setPageSize(3);

        JsonNode inputNode = objectMapper.valueToTree(input);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        GraphQLResponse graphQLResponse = client.perform(SEARCH_PRODUCTS_SHOP, variables);
        SearchResponse searchResponse = graphQLResponse.get("$.data.search", SearchResponse.class);

        List<String> variantNames =
                searchResponse.getItems().stream().map(i -> i.getProductVariantName()).collect(Collectors.toList());
        if ("name".equals(sortBy)) {
            assertThat(variantNames).containsExactlyInAnyOrder("USB Cable", "Tripod", "Tent");
        } else {
            assertThat(variantNames)
                    .containsExactlyInAnyOrder("Road Bike", "Laptop 15 inch 16GB", "Laptop 13 inch 16GB");
        }
    }


    /**
     * shop api
     */
    @Test
    @Order(1)
    public void total_items() throws IOException {
        testTotalItems(shopClient);
    }

    @Test
    @Order(2)
    public void matches_search_term() throws IOException {
        testMatchSearchTerm(shopClient);
    }

    @Test
    @Order(3)
    public void matches_by_facetId_with_AND_operator() throws IOException {
        testMatchFacetIdsAnd(shopClient);
    }

    @Test
    @Order(4)
    public void matches_by_facetId_with_OR_operator() throws IOException {
        testMatchFacetIdsOr(shopClient);
    }

    @Test
    @Order(5)
    public void matches_by_collectionId() throws IOException {
        testMatchCollectionId(shopClient);
    }

    @Test
    @Order(6)
    public void matches_by_collectionSlug() throws IOException {
        testMatchCollectionId(shopClient);
    }

    @Test
    @Order(7)
    public void prices() throws IOException {
        testPrices(shopClient);
    }

    @Test
    @Order(8)
    public void returns_correct_facetValues() throws IOException {
        SearchInput input = new SearchInput();

        JsonNode inputNode = objectMapper.valueToTree(input);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        GraphQLResponse graphQLResponse = shopClient.perform(SEARCH_GET_FACET_VALUES, variables);
        SearchResponse searchResponse = graphQLResponse.get("$.data.search", SearchResponse.class);

        assertThat(searchResponse.getFacetValues()).hasSize(6);

        FacetValueResult facetValueResult = searchResponse.getFacetValues().get(0);
        assertThat(facetValueResult.getCount()).isEqualTo(21);
        assertThat(facetValueResult.getFacetValue().getId()).isEqualTo(1L);
        assertThat(facetValueResult.getFacetValue().getName()).isEqualTo("electronics");

        facetValueResult = searchResponse.getFacetValues().get(1);
        assertThat(facetValueResult.getCount()).isEqualTo(17);
        assertThat(facetValueResult.getFacetValue().getId()).isEqualTo(2L);
        assertThat(facetValueResult.getFacetValue().getName()).isEqualTo("computers");

        facetValueResult = searchResponse.getFacetValues().get(2);
        assertThat(facetValueResult.getCount()).isEqualTo(4);
        assertThat(facetValueResult.getFacetValue().getId()).isEqualTo(3L);
        assertThat(facetValueResult.getFacetValue().getName()).isEqualTo("photo");

        facetValueResult = searchResponse.getFacetValues().get(3);
        assertThat(facetValueResult.getCount()).isEqualTo(10);
        assertThat(facetValueResult.getFacetValue().getId()).isEqualTo(4L);
        assertThat(facetValueResult.getFacetValue().getName()).isEqualTo("sports equipment");

        facetValueResult = searchResponse.getFacetValues().get(4);
        assertThat(facetValueResult.getCount()).isEqualTo(3);
        assertThat(facetValueResult.getFacetValue().getId()).isEqualTo(5L);
        assertThat(facetValueResult.getFacetValue().getName()).isEqualTo("home & garden");

        facetValueResult = searchResponse.getFacetValues().get(5);
        assertThat(facetValueResult.getCount()).isEqualTo(3);
        assertThat(facetValueResult.getFacetValue().getId()).isEqualTo(6L);
        assertThat(facetValueResult.getFacetValue().getName()).isEqualTo("plants");
    }

    @Test
    @Order(9)
    public void omits_facetValues_of_private_facets() throws IOException {
        CreateFacetInput input = new CreateFacetInput();
        input.setCode("profit-margin");
        input.setPrivateOnly(true);
        input.setName("Profit Margin");
        CreateFacetValueWithFacetInput createFacetValueWithFacetInput = new CreateFacetValueWithFacetInput();
        createFacetValueWithFacetInput.setCode("massive");
        createFacetValueWithFacetInput.setName("massive");
        input.getValues().add(createFacetValueWithFacetInput);

        JsonNode inputNode = objectMapper.valueToTree(input);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        GraphQLResponse graphQLResponse =
                adminClient.perform(CREATE_FACET, variables, Arrays.asList(FACET_WITH_VALUES_FRAGMENT, FACET_VALUE_FRAGMENT));
        Facet createdFacet = graphQLResponse.get("$.data.createFacet", Facet.class);

        UpdateProductInput updateProductInput = new UpdateProductInput();
        updateProductInput.setId(2L);
        // 1 & 2 are the existing facetValues (electronics & photo)
        updateProductInput.getFacetValueIds().addAll(Arrays.asList(1L, 2L, createdFacet.getValues().get(0).getId()));

        inputNode = objectMapper.valueToTree(updateProductInput);
        variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        adminClient.perform(UPDATE_PRODUCT, variables,
                        Arrays.asList(PRODUCT_WITH_VARIANTS_FRAGMENT, PRODUCT_VARIANT_FRAGMENT, ASSET_FRAGMENT));

        testHelper.awaitRunningTasks();

        returns_correct_facetValues();
    }

    @Test
    @Order(10)
    public void encodes_the_productId_and_productVariantId() throws IOException {
        SearchInput input = new SearchInput();
        input.setPageSize(1);

        JsonNode inputNode = objectMapper.valueToTree(input);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        GraphQLResponse graphQLResponse = shopClient.perform(SEARCH_PRODUCTS_SHOP, variables);
        SearchResponse searchResponse = graphQLResponse.get("$.data.search", SearchResponse.class);

        SearchResult searchResult = searchResponse.getItems().get(0);
        assertThat(searchResult.getProductId()).isEqualTo(1);
        assertThat(searchResult.getProductVariantId()).isEqualTo(1);
    }


    @Test
    @Order(11)
    public void sort_name() throws IOException {
        testSorting(shopClient, "name");
    }

    @Test
    @Order(12)
    public void sort_price() throws IOException {
        testSorting(shopClient, "price");
    }

    @Test
    @Order(13)
    public void omits_results_for_disabled_ProductVariants() throws IOException {
        UpdateProductVariantInput input = new UpdateProductVariantInput();
        input.setId(3L);
        input.setEnabled(false);

        JsonNode inputNode = objectMapper.valueToTree(input);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        adminClient.perform(UPDATE_PRODUCT_VARIANTS, variables, Arrays.asList(PRODUCT_VARIANT_FRAGMENT, ASSET_FRAGMENT));

        testHelper.awaitRunningTasks();

        SearchInput searchInput = new SearchInput();
        searchInput.setPageSize(3);

        inputNode = objectMapper.valueToTree(searchInput);
        variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        GraphQLResponse graphQLResponse = shopClient.perform(SEARCH_PRODUCTS_SHOP, variables);
        SearchResponse searchResponse = graphQLResponse.get("$.data.search", SearchResponse.class);

        assertThat(searchResponse.getItems().stream().map(i -> i.getProductVariantId()).collect(Collectors.toList()))
                .containsExactlyInAnyOrder(1L, 2L, 4L);
    }

    @Test
    @Order(14)
    public void encodes_collectionIds() throws IOException {
        SearchInput input = new SearchInput();
        input.setPageSize(1);
        input.setTerm("cactus");

        JsonNode inputNode = objectMapper.valueToTree(input);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        GraphQLResponse graphQLResponse = shopClient.perform(SEARCH_PRODUCTS_SHOP, variables);
        SearchResponse searchResponse = graphQLResponse.get("$.data.search", SearchResponse.class);

        assertThat(searchResponse.getItems().get(0).getCollectionIds()).containsExactly(2L);
    }
}
