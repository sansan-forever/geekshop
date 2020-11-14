/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.eventbus;

import co.jueyi.geekshop.email.EmailDetails;
import co.jueyi.geekshop.email.EmailSender;
import co.jueyi.geekshop.email.PebbleTemplateService;
import co.jueyi.geekshop.entity.AuthenticationMethodEntity;
import co.jueyi.geekshop.eventbus.events.PasswordResetEvent;
import co.jueyi.geekshop.exception.InternalServerError;
import co.jueyi.geekshop.exception.email.SendEmailException;
import co.jueyi.geekshop.exception.email.TemplateException;
import co.jueyi.geekshop.options.ConfigOptions;
import co.jueyi.geekshop.service.UserService;
import com.google.common.collect.ImmutableMap;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("Duplicates")
public class PasswordResetEventSubscriber {
    final static String PASSWORD_RESET_EMAIL_TEMPLATE = "password-reset";
    final static String PASSWORD_RESET_EMAIL_SUBJECT = "Forgotten password reset";

    private final ConfigOptions configOptions;
    private final PebbleTemplateService pebbleTemplateService;
    private final UserService userService;
    private final EventBus eventBus;
    private final EmailSender emailSender;

    @PostConstruct
    void init() {
        eventBus.register(this);
    }

    @Subscribe
    public void onEvent(PasswordResetEvent event) {
        log.info("onEvent called event = " + event);

        AuthenticationMethodEntity nativeAuthMethod = null;
        try {
            nativeAuthMethod = userService.getNativeAuthMethodEntityByUserId(event.getUserEntity().getId());
        } catch (InternalServerError ise) {
            log.warn("Error to get native auth method, userId = { " + event.getUserEntity().getId() + " }", ise);
            return;
        }

        EmailDetails emailDetails = new EmailDetails(
                event,
                event.getUserEntity().getIdentifier(),
                this.configOptions.getEmailOptions().getDefaultFromEmail());

        emailDetails.setSubject(PASSWORD_RESET_EMAIL_SUBJECT);

        Map<String, Object> model = ImmutableMap.of(
                "passwordResetToken", nativeAuthMethod.getPasswordResetToken(),
                "passwordResetUrl", configOptions.getEmailOptions().getPasswordResetUrl());
        try {
            String body = this.pebbleTemplateService.mergeTemplateIntoString(PASSWORD_RESET_EMAIL_TEMPLATE, model);
            emailDetails.setBody(body);
        } catch (TemplateException te) {
            log.error("The template file cannot be processed", te);
            throw new SendEmailException("Error while processing the template file with the given model object.", te);
        }
        emailDetails.getModel().putAll(model);// 仅方便测试用

        try {
            emailSender.send(emailDetails);
        } catch (Exception ex) {
            log.error("Exception to send the email", ex);
            throw new SendEmailException("Exception while sending the email.", ex);
        }
    }
}
