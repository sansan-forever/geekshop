/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.config.asset;

import co.jueyi.geekshop.exception.ErrorCode;
import co.jueyi.geekshop.exception.InternalServerError;

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
