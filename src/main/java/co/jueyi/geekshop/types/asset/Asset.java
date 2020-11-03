package co.jueyi.geekshop.types.asset;

import co.jueyi.geekshop.types.common.Node;
import lombok.Data;

import java.util.Date;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class Asset implements Node {
    public Long id;
    public Date createdAt;
    public Date updatedAt;
    public String name;
    public AssetType type;
    public Integer fileSize;
    public String mimeType;
    public Integer width;
    public Integer height;
    public String source;
    public String preview;
    public Coordinate focalPoint;
}
