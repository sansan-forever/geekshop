/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.data_import.asset_importer;

import io.geekshop.entity.AssetEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class AssetImportResult {
    private List<AssetEntity> assets = new ArrayList<>();
    private List<String> errors = new ArrayList<>();
}
