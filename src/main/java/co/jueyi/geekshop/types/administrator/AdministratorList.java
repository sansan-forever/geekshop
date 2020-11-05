package co.jueyi.geekshop.types.administrator;

import co.jueyi.geekshop.types.common.PaginatedList;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class AdministratorList implements PaginatedList<Administrator> {
    private List<Administrator> items = new ArrayList<>();
    private Integer totalItems;
}
