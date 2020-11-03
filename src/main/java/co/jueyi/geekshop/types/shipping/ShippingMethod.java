package co.jueyi.geekshop.types.shipping;

import co.jueyi.geekshop.types.common.ConfigurableOperation;
import co.jueyi.geekshop.types.common.Node;
import lombok.Data;

import java.util.Date;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class ShippingMethod implements Node {
    public Long id;
    public Date createdAt;
    public Date updatedAt;
    public String code;
    public String description;
    public ConfigurableOperation checker;
    public ConfigurableOperation calculator;
}
