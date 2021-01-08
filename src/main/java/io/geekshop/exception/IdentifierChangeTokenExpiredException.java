/*
 * Copyright (c) 2020 极客之道(daogeek.io).
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
