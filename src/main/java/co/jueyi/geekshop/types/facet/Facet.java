package co.jueyi.geekshop.types.facet;

import co.jueyi.geekshop.types.common.Node;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class Facet implements Node {
    public Long id;
    public Date createdAt;
    public Date updatedAt;
    public String name;
    public String code;
    public List<FacetValue> values = new ArrayList<>();
}
