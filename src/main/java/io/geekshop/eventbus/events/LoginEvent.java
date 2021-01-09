/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.eventbus.events;

import io.geekshop.common.RequestContext;
import io.geekshop.types.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * This event is fired when a user successfully logs in via the shop or admin API `login` mutation.
 *
 * Created on Nov, 2020 by @author bobo
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LoginEvent extends BaseEvent {
    private final RequestContext ctx;
    private final User uer;
}
