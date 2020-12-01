/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.data_import.asset_importer;

import co.jueyi.geekshop.entity.AssetEntity;
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
