package co.jueyi.geekshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * A grouping of {@link co.jueyi.geekshop.types.customer.Customer}'s which enables features such as
 * group-based promotions.
 *
 * Created on Nov, 2020 by @author bobo
 */
@TableName(value = "tb_customer_group")
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerGroupEntity extends BaseEntity {
    public String name;
}
