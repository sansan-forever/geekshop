package co.jueyi.geekshop.types.facet;

import co.jueyi.geekshop.types.common.Node;
import lombok.Data;

import java.util.Date;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class FacetValue implements Node {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private Facet facet;
    private String name;
    private String code;
}
