/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.email;

/**
 * Created on Nov, 2020 by @author bobo
 */
public interface EmailSender {
    void send(EmailDetails emailDetails) throws Exception;
}
