/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.custom.security.Allow;
import co.jueyi.geekshop.entity.CollectionEntity;
import co.jueyi.geekshop.service.CollectionService;
import co.jueyi.geekshop.types.collection.Collection;
import co.jueyi.geekshop.types.collection.CreateCollectionInput;
import co.jueyi.geekshop.types.collection.MoveCollectionInput;
import co.jueyi.geekshop.types.collection.UpdateCollectionInput;
import co.jueyi.geekshop.types.common.DeletionResponse;
import co.jueyi.geekshop.types.common.Permission;
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
