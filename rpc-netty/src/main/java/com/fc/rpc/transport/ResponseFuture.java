package com.fc.rpc.transport;

import com.fc.rpc.transport.command.Command;

import java.util.concurrent.CompletableFuture;

/**
 * response future
 *
 * @author feng.chuang
 * @date 2024-12-26 09:22
 */
public class ResponseFuture {
    private final int requestId;
    private final CompletableFuture<Command> future;
    private final long timestamp;

    public ResponseFuture(int requestId, CompletableFuture<Command> future) {
        this.requestId = requestId;
        this.future = future;
        this.timestamp = System.nanoTime();
    }

    public int getRequestId() {
        return requestId;
    }

    public CompletableFuture<Command> getFuture() {
        return future;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
