package co.jueyi.geekshop.types.shipping;

import co.jueyi.geekshop.types.common.PaginatedList;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class ShippingMethodList implements PaginatedList<ShippingMethod> {
    public List<ShippingMethod> items = new ArrayList<>();
    public Integer totalItems;
}
