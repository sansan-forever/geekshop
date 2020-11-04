package co.jueyi.geekshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * This entity represents a customer of the store, typically an individual person. A Customer can be
 * a guest, in which case it has no associated {@link co.jueyi.geekshop.types.user.User}. Customers with
 * registered account will have an associated User entity.
 *
 * Created on Nov, 2020 by @author bobo
 */
@TableName(value = "tb_customer")
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerEntity extends BaseEntity {
    public Date deletedAt;
    public String firstName;
    public String lastName;
    public String phoneNumber;
    public String emailAddress;
    public Long userId;
}
