/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.e2e;

import co.jueyi.geekshop.*;
import co.jueyi.geekshop.config.TestConfig;
import co.jueyi.geekshop.types.asset.Asset;
import co.jueyi.geekshop.types.asset.AssetList;
import co.jueyi.geekshop.types.asset.AssetType;
import co.jueyi.geekshop.types.common.SortOrder;
import co.jueyi.geekshop.types.common.StringOperators;
import co.jueyi.geekshop.types.facet.FacetValue;
import co.jueyi.geekshop.types.product.*;
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
 * Created on Dec, 2020 by @author bobo
 */
@GeekShopGraphQLTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
public class ProductTest {
    static final String SHARED_GRAPHQL_RESOURCE_TEMPLATE = "graphql/shared/%s.graphqls";
    static final String GET_PRODUCT_LIST =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "get_product_list");
    static final String GET_PRODUCT_LIST_BY_SHOP =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "get_product_list_by_shop");
    static final String GET_PRODUCT_SIMPLE =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "get_product_simple");
    static final String GET_PRODUCT_WITH_VARIANTS =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "get_product_with_variants");
    static final String ASSET_FRAGMENT =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "asset_fragment");
    static final String PRODUCT_VARIANT_FRAGMENT =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "product_variant_fragment");
    static final String PRODUCT_WITH_VARIANTS_FRAGMENT =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "product_with_variants_fragment");
    static final String CREATE_PRODUCT =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "create_product");
    static final String GET_ASSET_LIST =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "get_asset_list");
    static final String UPDATE_PRODUCT =
            String.format(SHARED_GRAPHQL_RESOURCE_TEMPLATE, "update_product");

    static final String PRODUCT_GRAPHQL_RESOURCE_TEMPLATE = "graphql/admin/product/%s.graphqls";
    static final String GET_PRODUCT_VARIANT =
            String.format(PRODUCT_GRAPHQL_RESOURCE_TEMPLATE, "get_product_variant");

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

    /**
     * products list query
     */

    @Test
    @Order(1)
    public void returns_all_products_when_no_options_passed() throws IOException {
        GraphQLResponse graphQLResponse = adminClient.perform(GET_PRODUCT_LIST, null);
        ProductList productList = graphQLResponse.get("$.data.adminProducts", ProductList.class);
        assertThat(productList.getItems()).hasSize(20);
        assertThat(productList.getTotalItems()).isEqualTo(20);
    }

    @Test
    @Order(2)
    public void limits_result_set_with_current_page_and_size() throws IOException {
        ProductListOptions options = new ProductListOptions();
        options.setCurrentPage(1);
        options.setPageSize(3);

        JsonNode optionsNode = objectMapper.valueToTree(options);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("options", optionsNode);

        GraphQLResponse graphQLResponse = adminClient.perform(GET_PRODUCT_LIST, variables);

        ProductList productList = graphQLResponse.get("$.data.adminProducts", ProductList.class);
        assertThat(productList.getItems()).hasSize(3);
        assertThat(productList.getTotalItems()).isEqualTo(20);
    }

    @Test
    @Order(3)
    public void filters_by_name_admin() throws IOException {
        ProductListOptions options = new ProductListOptions();
        ProductFilterParameter filterParameter = new ProductFilterParameter();
        StringOperators stringOperators = new StringOperators();
        stringOperators.setContains("skateboard");
        filterParameter.setName(stringOperators);
        options.setFilter(filterParameter);

        JsonNode optionsNode = objectMapper.valueToTree(options);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("options", optionsNode);

        GraphQLResponse graphQLResponse = adminClient.perform(GET_PRODUCT_LIST, variables);

        ProductList productList = graphQLResponse.get("$.data.adminProducts", ProductList.class);
        assertThat(productList.getItems()).hasSize(1);
        assertThat(productList.getItems().get(0).getName()).isEqualTo("Cruiser Skateboard");
    }

    @Test
    @Order(4)
    public void filters_multiple_admin() throws IOException {
        ProductListOptions options = new ProductListOptions();
        ProductFilterParameter filterParameter = new ProductFilterParameter();
        StringOperators stringOperators = new StringOperators();
        stringOperators.setContains("skateboard");
        filterParameter.setName(stringOperators);

        stringOperators = new StringOperators();
        stringOperators.setContains("tent");
        filterParameter.setSlug(stringOperators);

        options.setFilter(filterParameter);

        JsonNode optionsNode = objectMapper.valueToTree(options);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("options", optionsNode);

        GraphQLResponse graphQLResponse = adminClient.perform(GET_PRODUCT_LIST, variables);

        ProductList productList = graphQLResponse.get("$.data.adminProducts", ProductList.class);
        assertThat(productList.getItems()).isEmpty();
    }

    @Test
    @Order(5)
    public void sorts_by_name_admin() throws IOException {
        ProductListOptions options = new ProductListOptions();

        ProductSortParameter sortParameter = new ProductSortParameter();
        sortParameter.setName(SortOrder.ASC);

        options.setSort(sortParameter);

        JsonNode optionsNode = objectMapper.valueToTree(options);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("options", optionsNode);

        GraphQLResponse graphQLResponse = adminClient.perform(GET_PRODUCT_LIST, variables);

        ProductList productList = graphQLResponse.get("$.data.adminProducts", ProductList.class);
        assertThat(productList.getItems().stream()
                .map(p -> p.getName()).collect(Collectors.toList()))
                .containsExactly(
                        "Bonsai Tree",
                        "Boxing Gloves",
                        "Camera Lens",
                        "Clacky Keyboard",
                        "Cruiser Skateboard",
                        "Curvy Monitor",
                        "Football",
                        "Gaming PC",
                        "Hard Drive",
                        "Instant Camera",
                        "Laptop",
                        "Orchid",
                        "Road Bike",
                        "Running Shoe",
                        "Skipping Rope",
                        "Slr Camera",
                        "Spiky Cactus",
                        "Tent",
                        "Tripod",
                        "USB Cable"
                );
    }

    @Test
    @Order(6)
    public void filters_by_name_shop() throws IOException {
        ProductListOptions options = new ProductListOptions();
        ProductFilterParameter filterParameter = new ProductFilterParameter();
        StringOperators stringOperators = new StringOperators();
        stringOperators.setContains("skateboard");
        filterParameter.setName(stringOperators);
        options.setFilter(filterParameter);

        JsonNode optionsNode = objectMapper.valueToTree(options);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("options", optionsNode);

        GraphQLResponse graphQLResponse = adminClient.perform(GET_PRODUCT_LIST_BY_SHOP, variables);

        ProductList productList = graphQLResponse.get("$.data.products", ProductList.class);
        assertThat(productList.getItems()).hasSize(1);
        assertThat(productList.getItems().get(0).getName()).isEqualTo("Cruiser Skateboard");
    }

    @Test
    @Order(7)
    public void sorts_by_name_shop() throws IOException {
        ProductListOptions options = new ProductListOptions();

        ProductSortParameter sortParameter = new ProductSortParameter();
        sortParameter.setName(SortOrder.ASC);

        options.setSort(sortParameter);

        JsonNode optionsNode = objectMapper.valueToTree(options);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("options", optionsNode);

        GraphQLResponse graphQLResponse = shopClient.perform(GET_PRODUCT_LIST_BY_SHOP, variables);

        ProductList productList = graphQLResponse.get("$.data.products", ProductList.class);
        assertThat(productList.getItems().stream()
                .map(p -> p.getName()).collect(Collectors.toList()))
                .containsExactly(
                        "Bonsai Tree",
                        "Boxing Gloves",
                        "Camera Lens",
                        "Clacky Keyboard",
                        "Cruiser Skateboard",
                        "Curvy Monitor",
                        "Football",
                        "Gaming PC",
                        "Hard Drive",
                        "Instant Camera",
                        "Laptop",
                        "Orchid",
                        "Road Bike",
                        "Running Shoe",
                        "Skipping Rope",
                        "Slr Camera",
                        "Spiky Cactus",
                        "Tent",
                        "Tripod",
                        "USB Cable"
                );
    }

    /**
     * product query
     */

    @Test
    @Order(8)
    public void by_id() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", 2L);

        GraphQLResponse graphQLResponse = adminClient.perform(GET_PRODUCT_SIMPLE, variables);
        Product product = graphQLResponse.get("$.data.adminProduct", Product.class);
        assertThat(product.getId()).isEqualTo(2L);
    }

    @Test
    @Order(9)
    public void by_slug() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("slug", "curvy-monitor");

        GraphQLResponse graphQLResponse = adminClient.perform(GET_PRODUCT_SIMPLE, variables);
        Product product = graphQLResponse.get("$.data.adminProduct", Product.class);
        assertThat(product.getSlug()).isEqualTo("curvy-monitor");
    }

    @Test
    @Order(10)
    public void throws_if_neither_id_nor_slug_provided() throws IOException {
        try {
            adminClient.perform(GET_PRODUCT_SIMPLE, null);
        } catch (ApiException apiEx) {
            assertThat(apiEx.getErrorMessage()).isEqualTo("Either the Product id or slug must be provided");
        }
    }

    @Test
    @Order(11)
    public void throws_if_id_and_slug_do_not_refer_to_the_same_Product() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", 2L);
        variables.put("slug", "laptop");

        try {
            adminClient.perform(GET_PRODUCT_SIMPLE, variables);
        } catch (ApiException apiEx) {
            assertThat(apiEx.getErrorMessage()).isEqualTo("The provided id and slug refer to different Products");
        }
    }

    @Test
    @Order(12)
    public void returns_expected_properties() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", 2L);

        GraphQLResponse graphQLResponse = adminClient.perform(GET_PRODUCT_WITH_VARIANTS, variables,
                Arrays.asList(PRODUCT_WITH_VARIANTS_FRAGMENT, PRODUCT_VARIANT_FRAGMENT, ASSET_FRAGMENT));
        Product product = graphQLResponse.get("$.data.adminProduct", Product.class);
        assertThat(product.getAssets()).hasSize(1);
        Asset asset = product.getAssets().get(0);
        assertThat(asset.getFileSize()).isEqualTo(1680);
        assertThat(asset.getId()).isEqualTo(2L);
        assertThat(asset.getMimeType()).isEqualTo("image/jpeg");
        assertThat(asset.getName()).isEqualTo("alexandru-acea-686569-unsplash.jpg");
        assertThat(asset.getPreview()).isEqualTo("target/test-assets/alexandru-acea-686569-unsplash__preview.jpg");
        assertThat(asset.getSource()).isEqualTo("target/test-assets/alexandru-acea-686569-unsplash.jpg");
        assertThat(asset.getType()).isEqualTo(AssetType.IMAGE);

        assertThat(product.getDescription()).isEqualTo(
                "Discover a truly immersive viewing experience with this monitor curved more deeply than any other. " +
                        "Wrapping around your field of vision the 1,800 R screencreates a wider field of view, " +
                        "enhances depth perception, and minimises peripheral distractions to draw you deeper " +
                        "in to your content."
        );
        assertThat(product.getEnabled()).isTrue();
        assertThat(product.getFacetValues()).hasSize(2);
        FacetValue facetValue1 = product.getFacetValues().get(0);
        assertThat(facetValue1.getCode()).isEqualTo("electronics");
        assertThat(facetValue1.getId()).isEqualTo(1L);
        assertThat(facetValue1.getName()).isEqualTo("electronics");
        assertThat(facetValue1.getFacet().getId()).isEqualTo(1);
        assertThat(facetValue1.getFacet().getName()).isEqualTo("category");

        FacetValue facetValue2 = product.getFacetValues().get(1);
        assertThat(facetValue2.getCode()).isEqualTo("computers");
        assertThat(facetValue2.getId()).isEqualTo(2L);
        assertThat(facetValue2.getName()).isEqualTo("computers");
        assertThat(facetValue2.getFacet().getId()).isEqualTo(1);
        assertThat(facetValue2.getFacet().getName()).isEqualTo("category");

        Asset featuredAsset = product.getFeaturedAsset();
        assertThat(featuredAsset.getFileSize()).isEqualTo(1680);
        assertThat(featuredAsset.getId()).isEqualTo(2L);
        assertThat(featuredAsset.getMimeType()).isEqualTo("image/jpeg");
        assertThat(featuredAsset.getName()).isEqualTo("alexandru-acea-686569-unsplash.jpg");
        assertThat(featuredAsset.getPreview()).isEqualTo("target/test-assets/alexandru-acea-686569-unsplash__preview.jpg");
        assertThat(featuredAsset.getSource()).isEqualTo("target/test-assets/alexandru-acea-686569-unsplash.jpg");
        assertThat(featuredAsset.getType()).isEqualTo(AssetType.IMAGE);

        assertThat(product.getId()).isEqualTo(2L);
        assertThat(product.getName()).isEqualTo("Curvy Monitor");
        assertThat(product.getOptionGroups()).hasSize(1);
        ProductOptionGroup productOptionGroup = product.getOptionGroups().get(0);
        assertThat(productOptionGroup.getCode()).isEqualTo("curvy-monitor-monitor-size");
        assertThat(productOptionGroup.getId()).isEqualTo(3L);
        assertThat(productOptionGroup.getName()).isEqualTo("monitor size");

        assertThat(product.getSlug()).isEqualTo("curvy-monitor");
    }

    @Test
    @Order(13)
    public void ProductVariant_price_properties_are_correct() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", 2L);

        GraphQLResponse graphQLResponse = adminClient.perform(GET_PRODUCT_WITH_VARIANTS, variables,
                Arrays.asList(PRODUCT_WITH_VARIANTS_FRAGMENT, PRODUCT_VARIANT_FRAGMENT, ASSET_FRAGMENT));
        Product product = graphQLResponse.get("$.data.adminProduct", Product.class);
        assertThat(product.getVariants().get(0).getPrice()).isEqualTo(14374);
    }

    @Test
    @Order(14)
    public void returns_null_when_id_not_found() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", 999L);

        GraphQLResponse graphQLResponse = adminClient.perform(GET_PRODUCT_WITH_VARIANTS, variables,
                Arrays.asList(PRODUCT_WITH_VARIANTS_FRAGMENT, PRODUCT_VARIANT_FRAGMENT, ASSET_FRAGMENT));
        Product product = graphQLResponse.get("$.data.adminProduct", Product.class);
        assertThat(product).isNull();
    }

    @Test
    @Order(15)
    public void productVariant_query_by_id() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", 1L);

        GraphQLResponse graphQLResponse = adminClient.perform(GET_PRODUCT_VARIANT, variables);
        ProductVariant productVariant = graphQLResponse.get("$.data.productVariant", ProductVariant.class);
        assertThat(productVariant.getId()).isEqualTo(1L);
        assertThat(productVariant.getName()).isEqualTo("Laptop 13 inch 8GB");
    }

    @Test
    @Order(16)
    public void productVariant_returns_null_when_id_not_found() throws IOException {
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", 999L);

        GraphQLResponse graphQLResponse = adminClient.perform(GET_PRODUCT_VARIANT, variables);
        ProductVariant productVariant = graphQLResponse.get("$.data.productVariant", ProductVariant.class);
        assertThat(productVariant).isNull();
    }

    /**
     * product mutation
     */

    Product newProduct;
    Product newProductWithAssets;

    @Test
    @Order(17)
    public void createProduct_creates_a_new_Product() throws IOException {
        CreateProductInput createProductInput = new CreateProductInput();
        createProductInput.setName("en Baked Potato");
        createProductInput.setSlug("en Baked Potato");
        createProductInput.setDescription("A baked potato");

        JsonNode inputNode = objectMapper.valueToTree(createProductInput);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        GraphQLResponse graphQLResponse = adminClient.perform(CREATE_PRODUCT, variables,
                Arrays.asList(PRODUCT_WITH_VARIANTS_FRAGMENT, PRODUCT_VARIANT_FRAGMENT, ASSET_FRAGMENT));
        newProduct = graphQLResponse.get("$.data.createProduct", Product.class);

        assertThat(newProduct.getAssets()).isEmpty();
        assertThat(newProduct.getEnabled()).isTrue();
        assertThat(newProduct.getFacetValues()).isEmpty();
        assertThat(newProduct.getFeaturedAsset()).isNull();
        assertThat(newProduct.getId()).isEqualTo(21L);
        assertThat(newProduct.getName()).isEqualTo("en Baked Potato");
        assertThat(newProduct.getSlug()).isEqualTo("en-baked-potato");
        assertThat(newProduct.getVariants()).isEmpty();
        assertThat(newProduct.getDescription()).isEqualTo("A baked potato");
    }

    @Test
    @Order(18)
    public void createProduct_creates_a_new_Product_with_assets() throws IOException {
        GraphQLResponse graphQLResponse =
                adminClient.perform(GET_ASSET_LIST, null, Arrays.asList(ASSET_FRAGMENT));
        AssetList assetList = graphQLResponse.get("$.data.assets", AssetList.class);
        List<Long> assetIds = assetList.getItems().stream()
                .map(Asset::getId).collect(Collectors.toList()).subList(0, 2);
        Long featuredAssetId = assetList.getItems().get(0).getId();

        CreateProductInput createProductInput = new CreateProductInput();
        createProductInput.setAssetIds(assetIds);
        createProductInput.setFeaturedAssetId(featuredAssetId);
        createProductInput.setName("en Has Assets");
        createProductInput.setSlug("en-has-assets");
        createProductInput.setDescription("A product with assets");

        JsonNode inputNode = objectMapper.valueToTree(createProductInput);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        graphQLResponse = adminClient.perform(CREATE_PRODUCT, variables,
                Arrays.asList(PRODUCT_WITH_VARIANTS_FRAGMENT, PRODUCT_VARIANT_FRAGMENT, ASSET_FRAGMENT));
        Product createdProduct = graphQLResponse.get("$.data.createProduct", Product.class);

        assertThat(createdProduct.getAssets().stream().map(Asset::getId).collect(Collectors.toList()))
                .containsExactly(assetIds.toArray(new Long[0]));
        assertThat(createdProduct.getFeaturedAsset().getId()).isEqualTo(featuredAssetId);
        newProductWithAssets = createdProduct;
    }

    @Test
    @Order(19)
    public void updateProduct_updates_a_Product() throws IOException {
        UpdateProductInput updateProductInput = new UpdateProductInput();
        updateProductInput.setId(newProduct.getId());
        updateProductInput.setName("en Mashed Potato");
        updateProductInput.setSlug("en-mashed-potato");
        updateProductInput.setDescription("A blob of mashed potato");

        JsonNode inputNode = objectMapper.valueToTree(updateProductInput);
        ObjectNode variables = objectMapper.createObjectNode();
        variables.set("input", inputNode);

        GraphQLResponse graphQLResponse = adminClient.perform(UPDATE_PRODUCT, variables,
                Arrays.asList(PRODUCT_WITH_VARIANTS_FRAGMENT, PRODUCT_VARIANT_FRAGMENT, ASSET_FRAGMENT));
        Product updatedProduct = graphQLResponse.get("$.data.updateProduct", Product.class);
        assertThat(updatedProduct.getDescription()).isEqualTo(
                "A blob of mashed potato"
        );
    }
}
