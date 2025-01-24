package com.fc.rpc.client;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 *
 * @author since
 * @date 2025-01-24 13:43
 */
public class RequestIdSupport {
    private final static AtomicInteger nextRequestId = new AtomicInteger(0);
    public static int next() {
        return nextRequestId.getAndIncrement();
    }
}
