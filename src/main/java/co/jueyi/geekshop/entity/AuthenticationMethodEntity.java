package co.jueyi.geekshop.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on Nov, 2020 by @author bobo
 */
@TableName(value = "tb_auth_method")
@Data
@EqualsAndHashCode(callSuper = true)
public class AuthenticationMethodEntity extends BaseEntity {
    public Long userId;

    public boolean external; // external or native

    // begin for native auth
    public String identifier;
    public String passwordHash;
    public String verificationToken;
    public String passwordRestToken;
    /**
     * A token issued when a User requests to change their identifer (typically an email address)
     */
    public String identifierChangeToken;
    /**
     * When a request has been made to change the User's identifier, the new identifier
     * will be stored here until it has been verified, after which it will replace
     * the current value of the `identifier` field.
     */
    public String pendingIdentifier;
    // end for native auth

    // begin for external auth
    public String strategy;
    public String externalIdentifier;
    @TableField(typeHandler = JacksonTypeHandler.class)
    public Map<String, String> metadata = new HashMap<>();
    // end for external auth
}
