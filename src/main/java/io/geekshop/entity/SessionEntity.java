/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.entity;

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
    private String token;
    private Date expires;
    private boolean invalidated;
    private boolean anonymous;
    /**
     * The {@link io.geekshop.types.user.User} who has authenticated to create this session.
     */
    private Long userId;
    private Long activeOrderId;
    /**
     * The name of the {@link io.geekshop.config.auth.AuthenticationStrategy} used when
     * authenticating to create this session.
     */
    private String authenticationStrategy;
}
