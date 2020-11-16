/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.types.asset.Asset;
import co.jueyi.geekshop.types.asset.UpdateAssetInput;
import co.jueyi.geekshop.types.common.DeletionResponse;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.Part;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@RequiredArgsConstructor
public class AssetMutation implements GraphQLMutationResolver {
    /**
     * Create new Assets
     */
    public List<Asset> createAssets(List<Part> parts, DataFetchingEnvironment dfe) {
        return null; // TODO
    }

    /**
     * Create one new Asset
     */
    public Asset createAsset(Part part, DataFetchingEnvironment dfe) {
        return null; // TODO
    }

    /**
     * Update an existing Asset
     */
    public Asset updateAsset(UpdateAssetInput input, DataFetchingEnvironment dfe) {
        return null; // TODO
    }

    /**
     * Delete an Asset
     */
    public DeletionResponse deleteAsset(Long id, Boolean force, DataFetchingEnvironment dfe) {
        return null; // TODO
    }

    /**
     * Delete multiple Assets
     */
    public DeletionResponse deleteAssets(List<Long> ids, Boolean force, DataFetchingEnvironment dfe) {
        return null; // TODO
    }
}
