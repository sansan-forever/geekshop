package co.jueyi.geekshop.types.history;

import co.jueyi.geekshop.types.common.SortOrder;
import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class HistoryEntrySortParameter {
    private SortOrder id;
    private SortOrder createdAt;
    private SortOrder updatedAt;
}
