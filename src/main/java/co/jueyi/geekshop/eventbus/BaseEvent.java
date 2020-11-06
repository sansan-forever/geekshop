package co.jueyi.geekshop.eventbus;

import lombok.Data;

import java.util.Date;

/**
 * The base class for all events used by the EventBus system.
 *
 * Created on Nov, 2020 by @author bobo
 */
@Data
public abstract class BaseEvent {
    private final Date createdAt;

    protected BaseEvent() {
        this.createdAt = new Date();
    }
}
