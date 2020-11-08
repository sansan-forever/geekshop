package co.jueyi.geekshop.eventbus.events;

import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.entity.UserEntity;
import co.jueyi.geekshop.eventbus.BaseEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * This event is fired when a registered user successfully changes the identifer (ie email address)
 * associated with their account.
 *
 * Created on Nov, 2020 by @author bobo
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class IdentifierChangeEvent extends BaseEvent {
    private final RequestContext ctx;
    private final UserEntity userEntity;
    private final String oldIdentifier;
}
