package com.fc.rpc.server;

/**
 * service provider registry
 *
 * @author since
 * @date 2025-01-05 10:00
 **/
public interface ServiceProviderRegistry {
    <T> void addServiceProvider(Class<? extends T> serviceClass, T serviceProvider);
}