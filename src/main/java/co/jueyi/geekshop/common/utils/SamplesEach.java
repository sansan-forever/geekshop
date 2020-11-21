/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.common.utils;

import java.util.List;

/**
 * Returns true if and only if exactly one item for each
 * of the "groups" lists appears in the "sample" list.
 *
 * Created on Nov, 2020 by @author bobo
 */
public abstract class SamplesEach {
    public static <T> boolean samplesEach(List<T> sample, List<List<T>> groups) {
        if (sample.size() != groups.size()) {
            return false;
        }

        for(List<T> group : groups) {
            int foundCount = 0;
            for (T item : sample) {
                if (group.contains(item)) {
                    foundCount++;
                }
            }
            if (foundCount != 1) return false;
        }

        return true;
    }
}
