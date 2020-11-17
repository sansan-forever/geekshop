/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.config.asset;

import lombok.Data;

/**
 *
 * The AssetConfig define how assets (images and other files) are named and stored, and how preview images are generated.
 *
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class AssetConfig {
    /**
     * Defines how asset files and preview images are named before being saved.
     */
    private final AssetNamingStrategy assetNamingStrategy;

    /**
     * Defines the strategy used for creating preview images of uploaded assets.
     */
    private final AssetPreviewStrategy assetPreviewStrategy;

    /**
     *  Defines the strategy used for storing uploaded binary files.
     */
    private final AssetStorageStrategy assetStorageStrategy;
}
