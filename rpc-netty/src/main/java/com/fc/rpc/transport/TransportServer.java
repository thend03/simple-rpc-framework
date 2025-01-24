package com.fc.rpc.transport;

/**
 * transport server
 *
 * @author since
 * @date 2025-01-02 09:04
 **/
public interface TransportServer {
    void start( RequestHandlerRegistry requestHandlerRegistry ,int port) throws Exception;
    void stop();
}