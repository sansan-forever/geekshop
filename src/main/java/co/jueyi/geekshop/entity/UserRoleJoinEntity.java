package co.jueyi.geekshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created on Nov, 2020 by @author bobo
 */
@TableName("tb_user_role_join")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserRoleJoinEntity extends BaseEntity {
    public Long userId;
    public Long roleId;
}
