package co.jueyi.geekshop.types.promotion;

import co.jueyi.geekshop.types.common.ConfigurableOperation;
import co.jueyi.geekshop.types.common.Node;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class Promotion implements Node {
    public Long id;
    public Date createdAt;
    public Date updatedAt;
    public Date startsAt;
    public Date endsAt;
    public String couponCode;
    public Integer perCustomerUsageLimit;
    public String name;
    public Boolean enabled;
    public List<ConfigurableOperation> conditions = new ArrayList<>();
    public List<ConfigurableOperation> actions = new ArrayList<>();
}
