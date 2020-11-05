package co.jueyi.geekshop.types.search;

import co.jueyi.geekshop.types.asset.Coordinate;
import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class SearchResultAsset {
    private Long id;
    private String review;
    private Coordinate focalPoint;
}
