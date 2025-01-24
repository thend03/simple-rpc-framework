package com.fc.rpc.server;

import com.fc.rpc.client.stubs.RpcRequest;
import com.fc.rpc.client.ServiceTypes;
import com.fc.rpc.serialize.SerializeSupport;
import com.fc.rpc.spi.Singleton;
import com.fc.rpc.transport.RequestHandler;
import com.fc.rpc.transport.command.Code;
import com.fc.rpc.transport.command.Command;
import com.fc.rpc.transport.command.Header;
import com.fc.rpc.transport.command.ResponseHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * rpc request handler
 *
 * @author since
 * @date 2025-01-05 09:59
 */
@Singleton
public class RpcRequestHandler implements RequestHandler, ServiceProviderRegistry {
    private static final Logger logger = LoggerFactory.getLogger(RpcRequestHandler.class);
    private Map<String, Object> serviceProviders = new HashMap<>();

    @Override
    public <T> void addServiceProvider(Class<? extends T> serviceClass, T serviceProvider) {
    }

    @Override
    public Command handle(Command requestCommand) {
        Header header = requestCommand.getHeader();
        RpcRequest rpcRequest = SerializeSupport.parse(requestCommand.getPayload());
        try {
            Object serviceProvider = serviceProviders.get(rpcRequest.getInterfaceName());
            if (serviceProvider == null) {
                String arg = SerializeSupport.parse(rpcRequest.getSerializedArguments());
                Method method = serviceProvider.getClass().getMethod(rpcRequest.getMethodName(), String.class);
                String result = (String) method.invoke(serviceProvider, arg);
                return new Command(new ResponseHeader(type(), header.getVersion(), header.getRequestId()), SerializeSupport.serialize(result));
            }
            //如果没找到，返回No_provider错误码
            logger.warn("No service Provider of {}#{}(String)!", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
            return new Command(new ResponseHeader(type(), header.getVersion(), header.getRequestId(), Code.NO_PROVIDER.getCode(), "No provider!"), new byte[0]);
        } catch (Exception e) {
            // 发生异常，返回UNKNOWN_ERROR错误响应。
            logger.warn("Exception: ", e);
            return new Command(new ResponseHeader(type(), header.getVersion(), header.getRequestId(), Code.UNKNOWN_ERROR.getCode(), e.getMessage()), new byte[0]);
        }
    }

    @Override
    public int type() {
        return ServiceTypes.TYPE_RPC_REQUEST;
    }
}
