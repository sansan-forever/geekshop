/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.types.common;

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
