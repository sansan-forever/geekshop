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
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private String code;
    private String description;
    private ConfigurableOperation checker;
    private ConfigurableOperation calculator;
}
