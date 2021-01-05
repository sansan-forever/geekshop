/*
 * Copyright (c) 2021 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.service.helpers.search_strategy;

import co.jueyi.geekshop.types.asset.Coordinate;
import co.jueyi.geekshop.types.search.PriceRange;
import co.jueyi.geekshop.types.search.SearchResult;
import co.jueyi.geekshop.types.search.SearchResultAsset;
import co.jueyi.geekshop.types.search.SinglePrice;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created on Jan, 2021 by @author bobo
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SearchStrategyUtils {
    private final ObjectMapper objectMapper;

    public SearchResult mapToSearchResult(Map<String, Object> map) {
        SearchResult searchResult = new SearchResult();

        if (map.get("min_price") != null) {
            PriceRange priceRange = new PriceRange();
            priceRange.setMin((Integer) map.get("min_price"));
            priceRange.setMax((Integer) map.get("max_price"));
            searchResult.setPriceRange(priceRange);
        } else {
            SinglePrice singlePrice = new SinglePrice();
            singlePrice.setValue((Integer) map.get("price"));
            searchResult.setPrice(singlePrice);
        }

        if (map.get("product_asset_id") != null) {
            SearchResultAsset searchResultAsset = new SearchResultAsset();
            searchResultAsset.setId((Long) map.get("product_asset_id"));
            searchResultAsset.setPreview((String) map.get("product_preview"));
            String jsonString = (String) map.get("product_preview_focal_point");
            if (!StringUtils.isEmpty(jsonString)) {
                searchResultAsset.setFocalPoint(parseFocalPoint(jsonString));
            }
            searchResult.setProductAsset(searchResultAsset);
        }

        if (map.get("product_variant_asset_id") != null) {
            SearchResultAsset searchResultAsset = new SearchResultAsset();
            searchResultAsset.setId((Long) map.get("product_variant_asset_id"));
            searchResultAsset.setPreview((String) map.get("product_variant_preview"));
            String jsonString = (String) map.get("product_variant_preview_focal_point");
            if (!StringUtils.isEmpty(jsonString)) {
                searchResultAsset.setFocalPoint(parseFocalPoint(jsonString));
            }
            searchResult.setProductVariantAsset(searchResultAsset);
        }

        searchResult.setSku((String) map.get("sku"));
        searchResult.setSlug((String) map.get("slug"));
        searchResult.setEnabled((Boolean) map.get("enabled"));
        searchResult.setProductVariantId((Long) map.get("product_variant_id"));
        searchResult.setProductId((Long) map.get("product_id"));
        searchResult.setProductName((String) map.get("product_name"));
        searchResult.setProductVariantName((String) map.get("product_variant_name"));
        searchResult.setDescription((String) map.get("description"));

        String jsonString = (String) map.get("facet_ids");
        if (!StringUtils.isEmpty(jsonString)) {
            searchResult.setFacetIds(parseLongList(jsonString));
        }
        jsonString = (String) map.get("facet_value_ids");
        if (!StringUtils.isEmpty(jsonString)) {
            searchResult.setFacetValueIds(parseLongList(jsonString));
        }
        jsonString = (String) map.get("collection_ids");
        if (!StringUtils.isEmpty(jsonString)) {
            searchResult.setCollectionIds(parseLongList(jsonString));
        }

        searchResult.setScore((Float) map.get("score"));

        return searchResult;
    }

    private List<Long> parseLongList(String jsonString) {
        try {
            List<Long> idList = objectMapper.readValue(jsonString,
                    objectMapper.getTypeFactory().constructCollectionType(
                            List.class, Long.class));
            return idList;
        } catch (JsonProcessingException ex) {
            log.error("Fail to convert jsonString to List<Long>", ex);
        }
        return new ArrayList<>();
    }

    private Coordinate parseFocalPoint(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, Coordinate.class);
        } catch (JsonProcessingException ex) {
            log.error("Fail to convert jsonString to Coordinate", ex);
        }
        return null;
    }
}
