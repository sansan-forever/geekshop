/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.exception;

/**
 * Created on Nov, 2020 by @author bobo
 */
public class IdentifierChangeTokenExpiredException extends AbstractGraphqlException {
    public IdentifierChangeTokenExpiredException() {
        super(ErrorCode.EXPIRED_IDENTIFIER_CHANGE_TOKEN);
    }
}
