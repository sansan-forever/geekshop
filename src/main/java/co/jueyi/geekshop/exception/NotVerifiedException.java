/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.exception;

/**
 * This error should be thrown when `requireVerification` in {@link co.jueyi.geekshop.options.AuthOptions}
 * is set to `true` and an unverified user attempts to authenticate.
 *
 * Created on Nov, 2020 by @author bobo
 */
public class NotVerifiedException extends AbstractGraphqlException {
    public NotVerifiedException() {
        super(ErrorCode.NOT_VERIFIED);
    }
}
