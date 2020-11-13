/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.eventbus.events;

import co.jueyi.geekshop.common.RequestContext;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * This event is fired when an attempt is made to log in via the shop or admin API `login` mutation.
 * The `strategy` represents the name of the AuthenticationStrategy used in the login attempt.
 * If the `native` strategy is used, the additional `identifier` property will be available.
 *
 * Created on Nov, 2020 by @author bobo
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AttemptedLoginEvent extends BaseEvent {
    private final RequestContext ctx;
    private final String strategy;
    private final String identifier;
}
