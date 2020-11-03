package co.jueyi.geekshop.types.facet;

import co.jueyi.geekshop.types.common.Node;
import lombok.Data;

import java.util.Date;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class FacetValue implements Node {
    public Long id;
    public Date createdAt;
    public Date updatedAt;
    public Facet facet;
    public String name;
    public String code;
}
