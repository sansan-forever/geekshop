/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.email;

/**
 * Created on Nov, 2020 by @author bobo
 */
public interface EmailSender {
    void send(EmailDetails emailDetails) throws Exception;
}
