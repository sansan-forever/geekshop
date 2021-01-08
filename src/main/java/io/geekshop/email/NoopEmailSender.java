/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.email;

import lombok.extern.slf4j.Slf4j;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Slf4j
public class NoopEmailSender implements EmailSender {
    @Override
    public void send(EmailDetails emailDetails) throws Exception {
        log.debug("To be sent email details - ");
        log.debug(emailDetails.toString());
    }
}
