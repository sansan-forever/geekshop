/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.options;

import lombok.Data;

/**
 * Options related to importing & exporting data.
 *
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class ImportExportOptions {
    /**
     * The directory in which assets to be imported are located.
     */
    private String importAssetsDir;
}
