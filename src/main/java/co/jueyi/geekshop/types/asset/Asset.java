/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.asset;

import co.jueyi.geekshop.types.common.Node;
import lombok.Data;

import java.util.Date;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class Asset implements Node {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private String name;
    private AssetType type;
    private Integer fileSize;
    private String mimeType;
    private Integer width;
    private Integer height;
    private String source;
    private String preview;
    private Coordinate focalPoint;
}
