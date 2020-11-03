package co.jueyi.geekshop.types.history;

import co.jueyi.geekshop.types.administrator.Administrator;
import co.jueyi.geekshop.types.common.Node;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class HistoryEntry implements Node {
    public Long id;
    public Date createdAt;
    public Date updatedAt;
    public Boolean isPublic;
    public HistoryEntryType type;
    public Administrator administrator;
    public Map<String, String> data = new HashMap<>();
}
