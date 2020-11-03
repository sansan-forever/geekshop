package co.jueyi.geekshop.types.search;

import co.jueyi.geekshop.types.asset.Coordinate;
import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class SearchResultAsset {
    public Long id;
    public String review;
    public Coordinate focalPoint;
}
