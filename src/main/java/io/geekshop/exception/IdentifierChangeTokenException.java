/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.exception;

/**
 * This error should be thrown when an error occurs when attempting to update a User's identifier
 * (e.g. change email address).
 *
 * Created on Nov, 2020 by @author bobo
 */
public class IdentifierChangeTokenException extends AbstractGraphqlException {
    public IdentifierChangeTokenException() {
        super(ErrorCode.NOT_RECOGNIZED_IDETIFIER_CHANGE_TOKEN);
    }
}
