/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.search;

import io.geekshop.types.asset.Coordinate;
import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class SearchResultAsset {
    private Long id;
    private String preview;
    private Coordinate focalPoint;
}
