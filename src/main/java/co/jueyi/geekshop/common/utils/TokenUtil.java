/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.common.utils;

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
