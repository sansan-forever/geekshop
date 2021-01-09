/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.common.utils;

import io.geekshop.types.asset.AssetType;

/**
 * Created on Nov, 2020 by @author bobo
 */
public abstract class AssetUtil {
    /**
     * Returns the AssetType based on the mime type
     */
    public static AssetType getAssetType(String mimeType) {
        String type = mimeType.split("/")[0];
        switch (type) {
            case "image":
                return AssetType.IMAGE;
            case "video":
                return AssetType.VIDEO;
            default:
                return AssetType.BINARY;
        }
    }
}
