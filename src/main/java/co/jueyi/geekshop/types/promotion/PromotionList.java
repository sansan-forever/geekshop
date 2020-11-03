package co.jueyi.geekshop.types.promotion;

import co.jueyi.geekshop.types.common.PaginatedList;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class PromotionList implements PaginatedList<Promotion> {
    public List<Promotion> items = new ArrayList<>();
    public Integer totalItems;
}
