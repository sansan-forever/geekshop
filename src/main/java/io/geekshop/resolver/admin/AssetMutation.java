/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.resolver.admin;

import io.geekshop.common.RequestContext;
import io.geekshop.common.utils.BeanMapper;
import io.geekshop.custom.security.Allow;
import io.geekshop.entity.AssetEntity;
import io.geekshop.service.AssetService;
import io.geekshop.types.asset.Asset;
import io.geekshop.types.asset.UpdateAssetInput;
import io.geekshop.types.common.DeletionResponse;
import io.geekshop.types.common.Permission;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.Part;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class AssetMutation implements GraphQLMutationResolver {

    private final AssetService assetService;

    /**
     * Create new Assets
     */
    @Allow(Permission.CreateCatalog)
    public List<Asset> createAssets(List<Part> parts, DataFetchingEnvironment dfe) {
        /**
         *  TODO:
         *  Currently we validate _all_ mime types up-front due to limitations
         *  with the  existing error handling mechanisms.
         */
        this.assetService.validateInputMimeTypes(parts);
        /**
         * TODO:
         * Is there some way to parallelize this while still preserving
         * the order of files in the upload? Non-deterministic IDs mess up the e2e tests.
         */
        List<Asset> assets = new ArrayList<>();
        for(Part part : parts) {
            AssetEntity assetEntity =
                    this.assetService.create(RequestContext.fromDataFetchingEnvironment(dfe), part);
            assets.add(BeanMapper.map(assetEntity, Asset.class));
        }
        return assets;
    }

    /**
     * Create one new Asset
     */
    @Allow(Permission.CreateCatalog)
    public Asset createAsset(Part part, DataFetchingEnvironment dfe) {
        this.assetService.validateInputMimeTypes(Arrays.asList(part));
        AssetEntity assetEntity =
                this.assetService.create(RequestContext.fromDataFetchingEnvironment(dfe), part);
        return BeanMapper.map(assetEntity, Asset.class);
    }

    /**
     * Update an existing Asset
     */
    @Allow(Permission.UpdateCatalog)
    public Asset updateAsset(UpdateAssetInput input, DataFetchingEnvironment dfe) {
        AssetEntity assetEntity =
                this.assetService.update(RequestContext.fromDataFetchingEnvironment(dfe), input);
        return BeanMapper.map(assetEntity, Asset.class);
    }

    /**
     * Delete an Asset
     */
    @Allow(Permission.DeleteCatalog)
    public DeletionResponse deleteAsset(Long id, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        return this.assetService.delete(ctx, Arrays.asList(id));
    }

    /**
     * Delete multiple Assets
     */
    @Allow(Permission.DeleteCatalog)
    public DeletionResponse deleteAssets(List<Long> ids, DataFetchingEnvironment dfe) {
        RequestContext ctx = RequestContext.fromDataFetchingEnvironment(dfe);
        return this.assetService.delete(ctx, ids);
    }
}
