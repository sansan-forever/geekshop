/*
 * Copyright (c) 2021 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.service;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.entity.FacetEntity;
import co.jueyi.geekshop.entity.FacetValueEntity;
import co.jueyi.geekshop.eventbus.events.ReIndexEvent;
import co.jueyi.geekshop.mapper.FacetEntityMapper;
import co.jueyi.geekshop.mapper.FacetValueEntityMapper;
import co.jueyi.geekshop.service.helpers.search_strategy.SearchStrategy;
import co.jueyi.geekshop.types.common.SearchInput;
import co.jueyi.geekshop.types.facet.FacetValue;
import co.jueyi.geekshop.types.search.FacetValueResult;
import co.jueyi.geekshop.types.search.SearchResponse;
import co.jueyi.geekshop.types.search.SearchResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.eventbus.EventBus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Search for supported database. See the various SearchStrategy implementations for db-specific code.
 *
 * Created on Jan, 2021 by @author bobo
 */
@Service
@RequiredArgsConstructor
public class SearchService {
    private final SearchStrategy searchStrategy;
    private final FacetValueEntityMapper facetValueEntityMapper;
    private final FacetEntityMapper facetEntityMapper;
    private final EventBus eventBus;

    public SearchResponse search(SearchInput input) {
        return this.search(input, false);
    }

    /**
     * Perform a search according to the provided input arguments.
     */
    public SearchResponse search(SearchInput input, boolean enabledOnly) {
        List<SearchResult> items = this.searchStrategy.getSearchResults(input, enabledOnly);
        Integer totalItems = this.searchStrategy.getTotalCount(input, enabledOnly);

        SearchResponse response = new SearchResponse();
        response.setItems(items);
        response.setTotalItems(totalItems);

        return response;
    }

    public List<FacetValueResult> facetValues(SearchInput input, boolean publicOnly) {
        return this.facetValues(input, false, publicOnly);
    }

    /**
     * Return a list of all FacetValues which appear in the result set.
     */
    public List<FacetValueResult> facetValues(SearchInput input, boolean enabledOnly, boolean publicOnly) {
        Map<Long, Integer> facetValueIdsMap  = this.searchStrategy.getFacetValueIds(input, enabledOnly);
        List<FacetValueEntity> facetValueEntities =
                this.facetValueEntityMapper.selectBatchIds(facetValueIdsMap.keySet());
        if (CollectionUtils.isEmpty(facetValueEntities)) return new ArrayList<>();

        if (publicOnly) {
            Set<Long> facetIds = facetValueEntities.stream()
                    .map(FacetValueEntity::getFacetId).collect(Collectors.toSet());
            QueryWrapper<FacetEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(FacetEntity::isPrivateOnly, true).in(FacetEntity::getId, facetIds);

            List<FacetEntity> facetEntities = this.facetEntityMapper.selectList(null);

            Set<Long> privateOnlyFacetIds = this.facetEntityMapper.selectList(queryWrapper)
                    .stream().map(FacetEntity::getId).collect(Collectors.toSet());

            if (!CollectionUtils.isEmpty(privateOnlyFacetIds)) {
                // 过滤掉Facet是private的FacetValue
                facetValueEntities = facetValueEntities.stream()
                        .filter(facetValueEntity -> !privateOnlyFacetIds.contains(facetValueEntity.getFacetId()))
                        .collect(Collectors.toList());
            }
        }

        List<FacetValueResult> facetValueResults = facetValueEntities.stream().map(facetValueEntity -> {
            FacetValue facetValue = BeanMapper.map(facetValueEntity, FacetValue.class);
            Integer count = facetValueIdsMap.get(facetValue.getId());
            FacetValueResult result = new FacetValueResult();
            result.setFacetValue(facetValue);
            result.setCount(count);
            return result;
        }).collect(Collectors.toList());

        return facetValueResults;
    }

    public Boolean reindex() {
        this.eventBus.post(new ReIndexEvent());
        return true;
    }

}
