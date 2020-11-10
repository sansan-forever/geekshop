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
    private Long userId;

    private boolean external; // external or native

    // begin for native shared
    private String identifier;
    private String passwordHash;
    private String verificationToken;
    private String passwordResetToken;
    /**
     * A token issued when a User requests to change their identifer (typically an email address)
     */
    private String identifierChangeToken;
    /**
     * When a request has been made to change the User's identifier, the new identifier
     * will be stored here until it has been verified, after which it will replace
     * the current value of the `identifier` field.
     */
    private String pendingIdentifier;
    // end for native shared

    // begin for external shared
    private String strategy;
    private String externalIdentifier;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, String> metadata = new HashMap<>();
    // end for external shared
}
