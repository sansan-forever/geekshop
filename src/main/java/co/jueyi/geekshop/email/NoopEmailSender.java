/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.email;

/**
 * Created on Nov, 2020 by @author bobo
 */
public class NoopEmailSender implements EmailSender {
    @Override
    public void send(EmailDetails emailDetails) throws Exception {
        // noop
    }
}
