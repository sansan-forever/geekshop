package co.jueyi.geekshop.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * This is the base class from which all entities inherit.
 *
 * Created on Nov, 2020 by @author bobo
 */
@Getter
@Setter
public class BaseEntity {
    @TableId(value = "id", type = IdType.AUTO)
    public Long id;
    @TableField(fill = FieldFill.INSERT)
    public String createdBy;
    @TableField(fill = FieldFill.INSERT)
    public Date createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    public Date updatedBy;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    public Date updatedAt;
}
