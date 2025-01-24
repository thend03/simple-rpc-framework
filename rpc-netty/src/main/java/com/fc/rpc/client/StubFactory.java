package com.fc.rpc.client;

import com.fc.rpc.transport.Transport;

/**
 *
 *
 * @author since
 * @date 2025-01-24 13:21
 */
public interface StubFactory {
    <T> T createStub(Transport transport,Class<T> serviceClass);
}
