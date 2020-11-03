package co.jueyi.geekshop.types.product;

import co.jueyi.geekshop.types.common.Node;
import lombok.Data;

import java.util.Date;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class ProductOption implements Node {
    public Long id;
    public Date createdAt;
    public Date updatedAt;
    public String code;
    public String name;
    public Long groupId;
    public ProductOptionGroup group;
}
