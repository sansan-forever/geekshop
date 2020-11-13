/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.exception.email;

/**
 * Created on Nov, 2020 by @author bobo
 */
public class SendEmailException extends RuntimeException {
    public SendEmailException() {
        super();
    }

    public SendEmailException(final String message) {
        super(message);
    }

    public SendEmailException(final Throwable cause) {
        super(cause);
    }

    public SendEmailException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
