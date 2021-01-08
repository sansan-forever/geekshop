/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.eventbus.events;

import io.geekshop.common.RequestContext;
import io.geekshop.entity.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * This event is fired when a registered user requests to update the identifier (ie email address)
 * associated with the account.
 *
 * Created on Nov, 2020 by @author bobo
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class IdentifierChangeRequestEvent extends BaseEvent {
    private final RequestContext ctx;
    private final UserEntity userEntity;
}
