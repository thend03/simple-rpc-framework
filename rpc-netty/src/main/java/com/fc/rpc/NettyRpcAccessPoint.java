package com.fc.rpc;

import com.fc.rpc.client.StubFactory;
import com.fc.rpc.server.ServiceProviderRegistry;
import com.fc.rpc.spi.RpcAccessPoint;
import com.fc.rpc.spi.ServiceSupport;
import com.fc.rpc.transport.RequestHandlerRegistry;
import com.fc.rpc.transport.Transport;
import com.fc.rpc.transport.TransportClient;
import com.fc.rpc.transport.TransportServer;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

/**
 *
 *
 * @author since
 * @date 2025-01-25 10:19
 */
public class NettyRpcAccessPoint implements RpcAccessPoint {
    private final String host = "localhost";
    private final int port = 9999;
    private final URI uri = URI.create("rpc://" + host + ":" + port);
    private TransportServer server = null;
    private TransportClient client = ServiceSupport.load(TransportClient.class);
    private final Map<URI, Transport> clientMap = new ConcurrentHashMap<>();
    private final StubFactory stubFactory = ServiceSupport.load(StubFactory.class);
    private final ServiceProviderRegistry serviceProviderRegistry = ServiceSupport.load(ServiceProviderRegistry.class);

    @Override
    public <T> T getRemoteService(URI uri, Class<T> serviceClass) {
        Transport transport = clientMap.computeIfAbsent(uri, this::createTransport);
        return stubFactory.createStub(transport, serviceClass);
    }

    private Transport createTransport(URI uri) {
        try {
            return client.createTransport(new InetSocketAddress(uri.getHost(), uri.getPort()), 30000L);
        } catch (InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized <T> URI addServiceProvider(T service, Class<T> serviceClass) {
        serviceProviderRegistry.addServiceProvider(serviceClass, service);
        return uri;
    }

    @Override
    public synchronized Closeable startServer() throws Exception {
        if (server == null) {
            server = ServiceSupport.load(TransportServer.class);
            server.start(RequestHandlerRegistry.getInstance(), port);
        }
        return () -> {
            if (server != null) {
                server.stop();
            }
        };
    }

    @Override
    public void close() throws IOException {
        if (server != null) {
            server.stop();
        }
        client.close();
    }
}
