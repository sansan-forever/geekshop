package co.jueyi.geekshop.eventbus.events;

import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.eventbus.BaseEvent;
import co.jueyi.geekshop.types.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LoginEvent extends BaseEvent {
    private final RequestContext ctx;
    private final User uer;
}
