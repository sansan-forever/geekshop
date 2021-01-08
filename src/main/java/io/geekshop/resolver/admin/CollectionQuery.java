/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.resolver.admin;

import io.geekshop.common.utils.BeanMapper;
import io.geekshop.custom.security.Allow;
import io.geekshop.entity.CollectionEntity;
import io.geekshop.exception.UserInputException;
import io.geekshop.service.CollectionService;
import io.geekshop.types.collection.Collection;
import io.geekshop.types.collection.CollectionList;
import io.geekshop.types.collection.CollectionListOptions;
import io.geekshop.types.common.ConfigurableOperationDefinition;
import io.geekshop.types.common.Permission;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class CollectionQuery implements GraphQLQueryResolver {

    private final CollectionService collectionService;

    @Allow(Permission.ReadCatalog)
    public CollectionList adminCollections(CollectionListOptions options, DataFetchingEnvironment dfe) {
        return this.collectionService.findAll(options);
    }

    /**
     * Get a Collection either by id or slug. If neither id nor slug is specified, an error will result.
     */
    @Allow(Permission.ReadCatalog)
    public Collection adminCollection(Long id, String slug, DataFetchingEnvironment dfe) {
        if (id != null) {
            CollectionEntity collectionEntity = this.collectionService.findOne(id);
            if (collectionEntity == null) return null;
            if (slug != null && !Objects.equals(collectionEntity.getSlug(), slug)) {
                throw new UserInputException("The provided id and slug refer to different Collections");
            }
            return BeanMapper.map(collectionEntity, Collection.class);
        } else if (slug != null) {
            CollectionEntity collectionEntity = this.collectionService.findOneBySlug(slug);
            if (collectionEntity == null) return null;
            return BeanMapper.map(collectionEntity, Collection.class);
        } else {
            throw new UserInputException("Either the Collection id or slug must be provided");
        }
    }

    @Allow(Permission.ReadCatalog)
    public List<ConfigurableOperationDefinition> collectionFilters(DataFetchingEnvironment dfe) {
        return this.collectionService.getAvailableFilters();
    }
}
