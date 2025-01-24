package com.fc.rpc.client.stubs;

/**
 * rpc reuqest
 *
 * @author since
 * @date 2025-01-13 16:43
 */
public class RpcRequest {
    private String interfaceName;
    private String methodName;
    private byte[] serializedArguments;

    public RpcRequest(String interfaceName, byte[] serializedArguments, String methodName) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.serializedArguments = serializedArguments;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public byte[] getSerializedArguments() {
        return serializedArguments;
    }
}
