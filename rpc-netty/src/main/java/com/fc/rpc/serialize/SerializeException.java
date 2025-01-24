package com.fc.rpc.serialize;

/**
 * serialize exception
 *
 * @author feng.chuang
 * @date 2024-12-20 08:55
 */
public class SerializeException extends RuntimeException {
    public SerializeException(String msg) {
        super(msg);
    }

    public SerializeException(Throwable throwable) {
        super(throwable);
    }
}
