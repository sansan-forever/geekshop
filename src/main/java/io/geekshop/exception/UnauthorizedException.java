/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.exception;

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
