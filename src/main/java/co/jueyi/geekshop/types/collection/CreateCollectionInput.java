/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.collection;

import co.jueyi.geekshop.types.common.ConfigurableOperationInput;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class CreateCollectionInput {
    private Boolean visibleToPublic;
    private Long featuredAssetId;
    private List<Long> assetIds = new ArrayList<>();
    private Long parentId;
    private List<ConfigurableOperationInput> filters = new ArrayList<>();
    private String name;
    private String slug;
    private String description;
}
