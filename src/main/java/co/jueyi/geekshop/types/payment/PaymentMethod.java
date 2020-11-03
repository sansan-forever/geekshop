package co.jueyi.geekshop.types.payment;

import co.jueyi.geekshop.types.common.ConfigArg;
import co.jueyi.geekshop.types.common.ConfigurableOperationDefinition;
import co.jueyi.geekshop.types.common.Node;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class PaymentMethod implements Node {
    public Long id;
    public Date createdAt;
    public Date updatedAt;
    public String code;
    public Boolean enabled;
    public List<ConfigArg> configArgs = new ArrayList<>();
    public ConfigurableOperationDefinition definition;
}
