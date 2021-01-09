/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.eventbus.events;

import io.geekshop.common.RequestContext;
import io.geekshop.entity.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * This event is fired when a Customer requests a password reset email.
 *
 * Created on Nov, 2020 by @author bobo
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PasswordResetEvent extends BaseEvent {
    private final RequestContext ctx;
    private final UserEntity userEntity;
}
