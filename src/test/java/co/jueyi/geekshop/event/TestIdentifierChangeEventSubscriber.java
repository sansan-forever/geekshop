/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.event;

import co.jueyi.geekshop.email.EmailDetails;
import co.jueyi.geekshop.email.EmailSender;
import co.jueyi.geekshop.eventbus.events.IdentifierChangeEvent;
import co.jueyi.geekshop.exception.email.SendEmailException;
import co.jueyi.geekshop.options.ConfigOptions;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class TestIdentifierChangeEventSubscriber {
    private final EventBus eventBus;
    private final EmailSender emailSender;
    private final ConfigOptions configOptions;

    @PostConstruct
    void init() {
        eventBus.register(this);
    }

    @Subscribe
    public void onEvent(IdentifierChangeEvent event) {
        EmailDetails emailDetails = new EmailDetails(
                event,
                event.getUserEntity().getIdentifier(),
                this.configOptions.getEmailOptions().getDefaultFromEmail());

        try {
            emailSender.send(emailDetails);
        } catch (Exception ex) {
            log.error("Exception to send the email", ex);
            throw new SendEmailException("Exception while sending the email.", ex);
        }
    }
}
