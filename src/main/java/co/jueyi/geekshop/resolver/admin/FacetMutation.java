/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.custom.security.Allow;
import co.jueyi.geekshop.entity.FacetEntity;
import co.jueyi.geekshop.entity.FacetValueEntity;
import co.jueyi.geekshop.service.FacetService;
import co.jueyi.geekshop.service.FacetValueService;
import co.jueyi.geekshop.types.common.DeletionResponse;
import co.jueyi.geekshop.types.common.Permission;
import co.jueyi.geekshop.types.facet.*;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class FacetMutation implements GraphQLMutationResolver {

    private final FacetService facetService;
    private final FacetValueService facetValueService;

    /**
     * Create a new Facet
     */
    @Allow(Permission.CreateCatalog)
    public Facet createFacet(CreateFacetInput input, DataFetchingEnvironment dfe) {
        FacetEntity facetEntity = this.facetService.create(input);
        return BeanMapper.map(facetEntity, Facet.class);
    }

    /**
     * Update an existing Facet
     */
    @Allow(Permission.UpdateCatalog)
    public Facet updateFacet(UpdateFacetInput input, DataFetchingEnvironment dfe) {
        FacetEntity facetEntity = this.facetService.update(input);
        return BeanMapper.map(facetEntity, Facet.class);
    }

    /**
     * Delete an existing Facet
     */
    @Allow(Permission.DeleteCatalog)
    public DeletionResponse deleteFacet(Long id, DataFetchingEnvironment dfe) {
        return this.facetService.delete(id);
    }

    /**
     * Create one or more FacetValues
     */
    @Allow(Permission.CreateCatalog)
    public List<FacetValue> createFacetValues(List<CreateFacetValueInput> input, DataFetchingEnvironment dfe) {
        List<FacetValue> facetValueList = new ArrayList<>();
        input.forEach(createFacetValueInput -> {
            FacetValueEntity facetValueEntity = this.facetValueService.create(createFacetValueInput);
            FacetValue facetValue = BeanMapper.map(facetValueEntity, FacetValue.class);
            facetValueList.add(facetValue);
        });
        return facetValueList;
    }

    /**
     * Update one or more FacetValues
     */
    @Allow(Permission.UpdateCatalog)
    public List<FacetValue> updateFacetValues(List<UpdateFacetValueInput> input, DataFetchingEnvironment dfe) {
        List<FacetValue> facetValueList = new ArrayList<>();
        input.forEach(updateFacetValueInput -> {
            FacetValueEntity facetValueEntity = this.facetValueService.update(updateFacetValueInput);
            FacetValue facetValue = BeanMapper.map(facetValueEntity, FacetValue.class);
            facetValueList.add(facetValue);
        });
        return facetValueList;
    }

    /**
     * Delete one or more FacetValues
     */
    @Allow(Permission.DeleteCatalog)
    public List<DeletionResponse> deleteFacetValues(List<Long> ids, DataFetchingEnvironment dfe) {
        List<DeletionResponse> deletionResponseList = new ArrayList<>();
        ids.forEach(id -> {
            DeletionResponse deletionResponse =
                    this.facetValueService.delete(id);
            deletionResponseList.add(deletionResponse);
        });
        return deletionResponseList;
    }
}
