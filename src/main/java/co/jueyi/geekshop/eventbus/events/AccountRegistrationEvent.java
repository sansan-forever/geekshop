package co.jueyi.geekshop.eventbus.events;

import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.eventbus.BaseEvent;
import co.jueyi.geekshop.types.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * This event is fired when a new user registers an account, either as a stand-alone signup or after
 * placing an order.
 *
 * Created on Nov, 2020 by @author bobo
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AccountRegistrationEvent extends BaseEvent {
    private final RequestContext ctx;
    private final User uer;
}
