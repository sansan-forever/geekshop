package co.jueyi.geekshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represent's a {@link co.jueyi.geekshop.types.customer.Customer}'s address.
 *
 * Created on Nov, 2020 by @author bobo
 */
@TableName(value = "tb_address")
@Data
@EqualsAndHashCode(callSuper = true)
public class AddressEntity extends BaseEntity {
    public Long customerId;
    public String fullName = "";
    public String company = "";
    public String streetLine1;
    public String streetLine2 = "";
    public String city = "";
    public String province = "";
    public String postalCode = "";
    public String phoneNumber = "";
    public boolean defaultShippingAddress;
    public boolean defaultBillingAddress;
}
