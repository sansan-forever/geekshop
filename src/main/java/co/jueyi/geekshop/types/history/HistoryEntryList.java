package co.jueyi.geekshop.types.history;

import co.jueyi.geekshop.types.common.PaginatedList;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class HistoryEntryList implements PaginatedList<HistoryEntry> {
    public List<HistoryEntry> items = new ArrayList<>();
    public Integer totalItems;
}
