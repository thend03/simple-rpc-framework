package com.fc.rpc.transport;

import com.fc.rpc.transport.command.Command;

import java.util.concurrent.CompletableFuture;

/**
 * transport
 *
 * @author since
 * @date 2024-12-25 17:55
 */
public interface Transport {
    CompletableFuture<Command> send(Command request);
}
