/*
 * Copyright (c) 2020 极客之道(daogeek.io).
 * All rights reserved.
 */

package io.geekshop.common.utils;

import java.security.SecureRandom;

/**
 * Created on Nov, 2020 by @author bobo
 */
public class TokenUtil {
    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    public static String generateNewToken(int size) {
        byte[] randomBytes = new byte[size];
        secureRandom.nextBytes(randomBytes);
        return HexUtil.bytesToHex(randomBytes);
    }
}
