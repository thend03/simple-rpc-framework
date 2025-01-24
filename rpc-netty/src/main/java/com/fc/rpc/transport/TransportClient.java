package com.fc.rpc.transport;

import java.io.Closeable;
import java.net.SocketAddress;
import java.util.concurrent.TimeoutException;

/**
 * transport client
 *
 * @author since
 * @date 2025-01-02 08:58
 **/
public interface TransportClient extends Closeable {
    Transport createTransport(SocketAddress address,long connectTimeout) throws InterruptedException, TimeoutException;

    @Override
    void close();
}