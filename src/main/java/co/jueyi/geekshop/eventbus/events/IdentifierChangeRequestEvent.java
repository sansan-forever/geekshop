package co.jueyi.geekshop.eventbus.events;

import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.entity.UserEntity;
import co.jueyi.geekshop.eventbus.BaseEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created on Nov, 2020 by @author bobo
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class IdentifierChangeRequestEvent extends BaseEvent {
    private final RequestContext ctx;
    private final UserEntity userEntity;
}
