/*
 * Copyright (c) 2020 GeekXYZ.
 * All rights reserved.
 */

package io.geekshop.types.common;

/**
 * Created on Nov, 2020 by @author bobo
 */
public enum DeletionResult {
    /**
     * The entity was successfully deleted
     */
    DELETED,
    /**
     * Deletion did not take place, reason given in message
     */
    NOT_DELETED
}
