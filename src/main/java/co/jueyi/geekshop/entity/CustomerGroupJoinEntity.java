package co.jueyi.geekshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created on Nov, 2020 by @author bobo
 */
@TableName("tb_customer_group_join")
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerGroupJoinEntity extends BaseEntity {
    public Long customerId;
    public Long groupId;
}
