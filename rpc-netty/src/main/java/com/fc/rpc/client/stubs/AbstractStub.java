package com.fc.rpc.client.stubs;

import com.fc.rpc.client.RequestIdSupport;
import com.fc.rpc.client.ServiceStub;
import com.fc.rpc.client.ServiceTypes;
import com.fc.rpc.serialize.SerializeSupport;
import com.fc.rpc.transport.Transport;
import com.fc.rpc.transport.command.Code;
import com.fc.rpc.transport.command.Command;
import com.fc.rpc.transport.command.Header;
import com.fc.rpc.transport.command.ResponseHeader;

import java.util.concurrent.ExecutionException;

/**
 *
 *
 * @author since
 * @date 2025-01-24 14:05
 */
public class AbstractStub implements ServiceStub {
    protected Transport transport;

    protected byte [] invokeRemote(RpcRequest request) {
        Header header = new Header(ServiceTypes.TYPE_RPC_REQUEST, 1, RequestIdSupport.next());
        byte [] payload = SerializeSupport.serialize(request);
        Command requestCommand = new Command(header, payload);
        try {
            Command responseCommand = transport.send(requestCommand).get();
            ResponseHeader responseHeader = (ResponseHeader) responseCommand.getHeader();
            if(responseHeader.getCode() == Code.SUCCESS.getCode()) {
                return responseCommand.getPayload();
            } else {
                throw new Exception(responseHeader.getError());
            }

        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setTransport(Transport transport) {
        this.transport = transport;
    }
}
