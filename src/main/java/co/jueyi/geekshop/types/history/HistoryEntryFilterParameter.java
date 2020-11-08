package co.jueyi.geekshop.types.history;

import co.jueyi.geekshop.types.common.BooleanOperators;
import co.jueyi.geekshop.types.common.DateOperators;
import co.jueyi.geekshop.types.common.StringOperators;
import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class HistoryEntryFilterParameter {
    private DateOperators createdAt;
    private DateOperators updatedAt;
    private BooleanOperators isPublic;
    private StringOperators type;
}
