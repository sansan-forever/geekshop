/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.custom.security.Allow;
import co.jueyi.geekshop.entity.FacetEntity;
import co.jueyi.geekshop.service.FacetService;
import co.jueyi.geekshop.types.common.Permission;
import co.jueyi.geekshop.types.facet.Facet;
import co.jueyi.geekshop.types.facet.FacetList;
import co.jueyi.geekshop.types.facet.FacetListOptions;
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
