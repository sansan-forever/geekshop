package co.jueyi.geekshop.types.product;

import co.jueyi.geekshop.types.common.Node;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class ProductOptionGroup implements Node {
    public Long id;
    public Date createdAt;
    public Date updatedAt;
    public String code;
    public String name;
    public List<ProductOption> options;
}
