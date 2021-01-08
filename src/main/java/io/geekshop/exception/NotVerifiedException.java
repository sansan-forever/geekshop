/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.exception;

/**
 * This error should be thrown when `requireVerification` in {@link io.geekshop.options.AuthOptions}
 * is set to `true` and an unverified user attempts to authenticate.
 *
 * Created on Nov, 2020 by @author bobo
 */
public class NotVerifiedException extends AbstractGraphqlException {
    public NotVerifiedException() {
        super(ErrorCode.NOT_VERIFIED);
    }
}
