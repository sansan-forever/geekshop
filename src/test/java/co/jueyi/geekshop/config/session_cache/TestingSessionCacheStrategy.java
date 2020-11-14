/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.config.session_cache;

import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * Created on Nov, 2020 by @author bobo
 */
@RequiredArgsConstructor
public class TestingSessionCacheStrategy implements SessionCacheStrategy {

    private final Map<String, CachedSession> theSessionCache;

    @Override
    public void set(CachedSession session) {
        theSessionCache.put(session.getToken(), session);
    }

    @Override
    public CachedSession get(String sessionToken) {
        return theSessionCache.get(sessionToken);
    }

    @Override
    public void delete(String sessionToken) {
        theSessionCache.remove(sessionToken);
    }

    @Override
    public void clear() {
        theSessionCache.clear();
    }
}
