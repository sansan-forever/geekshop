/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.exception;

/**
 * This error should be thrown when the user's authentication credentials do not match.
 *
 * Created on Nov, 2020 by @author bobo
 */
public class UnauthorizedException extends AbstractGraphqlException {
    public UnauthorizedException() {
        super(ErrorCode.UNAUTHORIZED);
    }
}
