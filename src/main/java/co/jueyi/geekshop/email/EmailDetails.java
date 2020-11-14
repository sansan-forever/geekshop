/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.email;

import co.jueyi.geekshop.eventbus.events.BaseEvent;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * The EmailData contains all the necessary data required to generate an email(subject, body).
 *
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class EmailDetails {
    private final BaseEvent event;
    private final String recipient;
    private final String from;
    private String body;
    private String subject;
    // 用于渲染模版的模型数据，仅用于方便测试
    private Map<String, Object> model = new HashMap<>();

    public EmailDetails(BaseEvent event, String recipient, String from) {
        this.event = event;
        this.recipient = recipient;
        this.from = from;
    }
}
