package co.jueyi.geekshop.entity;

import co.jueyi.geekshop.types.common.Permission;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * A Role represents a collection of permissions which determine the authorization
 * level of a {@link co.jueyi.geekshop.types.user.User}.
 *
 * Created on Nov, 2020 by @author bobo
 */
@TableName(value = "tb_role")
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleEntity extends BaseEntity {
    public String code = "";
    public String description = "";
    @TableField(typeHandler = JacksonTypeHandler.class)
    public List<Permission> permissions = new ArrayList<>();
}
