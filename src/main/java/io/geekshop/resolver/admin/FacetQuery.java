/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.resolver.admin;

import io.geekshop.common.utils.BeanMapper;
import io.geekshop.custom.security.Allow;
import io.geekshop.entity.FacetEntity;
import io.geekshop.service.FacetService;
import io.geekshop.types.common.Permission;
import io.geekshop.types.facet.Facet;
import io.geekshop.types.facet.FacetList;
import io.geekshop.types.facet.FacetListOptions;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class FacetQuery implements GraphQLQueryResolver {
    private final FacetService facetService;

    @Allow(Permission.ReadCatalog)
    public FacetList facets(FacetListOptions options, DataFetchingEnvironment dfe) {
        return this.facetService.findAll(options);
    }

    @Allow(Permission.ReadCatalog)
    public Facet facet(Long id, DataFetchingEnvironment dfe) {
        FacetEntity facetEntity = this.facetService.findOne(id);
        if (facetEntity == null) return null;
        return BeanMapper.map(facetEntity, Facet.class);
    }
}
