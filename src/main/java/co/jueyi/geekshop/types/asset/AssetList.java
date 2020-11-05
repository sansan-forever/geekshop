package co.jueyi.geekshop.types.asset;

import co.jueyi.geekshop.types.common.PaginatedList;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class AssetList implements PaginatedList<Asset> {
    private List<Asset> items = new ArrayList<>();
    private Integer totalItems;
}
