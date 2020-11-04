package co.jueyi.geekshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * A Session is created when a user makes a request to the API. A Session can be an AnonymousSession
 * in the case of un-authenticated users, otherwise it is an AuthenticatedSession.
 *
 * Created on Nov, 2020 by @author bobo
 */
@TableName(value = "tb_session")
@Data
@EqualsAndHashCode(callSuper = true)
public class SessionEntity extends BaseEntity {
    public String token;
    public Date expires;
    public boolean invalided;
    public boolean anonymous;
    /**
     * The {@link co.jueyi.geekshop.types.user.User} who has authenticated to create this session.
     */
    public Long userId;
    public Long activeOrderId;
    /**
     * The name of the {@link AuthenticationStrategyEntity} used when authenticating to create this session.
     */
    public String authenticationStrategy;
}
