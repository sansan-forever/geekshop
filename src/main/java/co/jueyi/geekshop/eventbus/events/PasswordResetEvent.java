/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.eventbus.events;

import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.entity.UserEntity;
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
