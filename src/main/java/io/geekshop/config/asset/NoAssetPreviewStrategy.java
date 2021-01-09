/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.config.asset;

import io.geekshop.exception.ErrorCode;
import io.geekshop.exception.InternalServerError;

/**
 * A placeholder strategy which will simply throw an error when used.
 *
 * Created on Nov, 2020 by @author bobo
 */
public class NoAssetPreviewStrategy implements AssetPreviewStrategy {
    @Override
    public byte[] generatePreviewImage(String mimeType, byte[] data) {
        throw new InternalServerError(ErrorCode.NO_ASSET_PREVIEW_STRATEGY_CONFIGURED);
    }
}
