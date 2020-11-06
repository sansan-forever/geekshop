package co.jueyi.geekshop.types.role;

import co.jueyi.geekshop.types.common.DateOperators;
import co.jueyi.geekshop.types.common.StringOperators;
import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class RoleFilterParameter {
    private StringOperators code;
    private StringOperators description;
    private DateOperators createdAt;
    private DateOperators updatedAt;
}
