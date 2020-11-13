/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.exception.email;

/**
 * Created on Nov, 2020 by @author bobo
 */
public class TemplateException extends Exception {
    public TemplateException() {
        super();
    }

    public TemplateException(final String message) {
        super(message);
    }

    public TemplateException(final Throwable cause) {
        super(cause);
    }

    public TemplateException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
