/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.common.utils;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

/**
 * Created on Nov, 2020 by @author bobo
 */
public abstract class IdUtil {
    public static String generatePublicId() {
        return NanoIdUtils.randomNanoId(
                NanoIdUtils.DEFAULT_NUMBER_GENERATOR,
                "123456789ABCDEFGHJKLMNPQRSTUVWXYZ".toCharArray(),
                16
        );
    }
}
