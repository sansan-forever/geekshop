/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.resolver.admin;

import io.geekshop.common.RequestContext;
import io.geekshop.common.utils.BeanMapper;
import io.geekshop.custom.security.Allow;
import io.geekshop.entity.CollectionEntity;
import io.geekshop.service.CollectionService;
import io.geekshop.types.collection.Collection;
import io.geekshop.types.collection.CreateCollectionInput;
import io.geekshop.types.collection.MoveCollectionInput;
import io.geekshop.types.collection.UpdateCollectionInput;
import io.geekshop.types.common.DeletionResponse;
import io.geekshop.types.common.Permission;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class CollectionMutation implements GraphQLMutationResolver {

    private final CollectionService collectionService;

    /**
     * Create a new Collection
     */
    @Allow(Permission.CreateCatalog)
    public Collection createCollection(CreateCollectionInput input, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        CollectionEntity collectionEntity = this.collectionService.create(ctx, input);
        return BeanMapper.map(collectionEntity, Collection.class);
    }

    /**
     * Update an existing Collection
     */
    @Allow(Permission.UpdateCatalog)
    public Collection updateCollection(UpdateCollectionInput input, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        CollectionEntity collectionEntity = this.collectionService.update(ctx, input);
        return BeanMapper.map(collectionEntity, Collection.class);
    }

    /**
     * Delete a Collection and all of its descendents
     */
    @Allow(Permission.DeleteCatalog)
    public DeletionResponse deleteCollection(Long id, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        return this.collectionService.delete(ctx, id);
    }

    /**
     * Move a Collection to a different parent or index
     */
    @Allow(Permission.UpdateCatalog)
    public Collection moveCollection(MoveCollectionInput input, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        CollectionEntity collectionEntity = this.collectionService.move(ctx, input);
        return BeanMapper.map(collectionEntity, Collection.class);
    }
}
