package co.jueyi.geekshop.eventbus.events;

import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.eventbus.BaseEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 * This event is fired when a user logs out via the shop or admin API `logout` mutation.
 *
 * Created on Nov, 2020 by @author bobo
 */
@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
public class LogoutEvent extends BaseEvent {
    private final RequestContext ctx;
}
