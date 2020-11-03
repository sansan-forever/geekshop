package co.jueyi.geekshop.types.role;

import co.jueyi.geekshop.types.common.PaginatedList;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class RoleList implements PaginatedList<Role> {
    public List<Role> items = new ArrayList<>();
    public Integer totalItems;
}
