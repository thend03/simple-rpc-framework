package com.fc.rpc.transport.netty;

import com.fc.rpc.transport.InFlightRequests;
import com.fc.rpc.transport.ResponseFuture;
import com.fc.rpc.transport.Transport;
import com.fc.rpc.transport.command.Command;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

import java.util.concurrent.CompletableFuture;

/**
 * netty transport
 *
 * @author feng.chuang
 * @date 2024-12-26 08:49
 */
public class NettyTransport implements Transport {
    private final Channel channel;
    private final InFlightRequests inFlightRequests;

    public NettyTransport(Channel channel, InFlightRequests inFlightRequests) {
        this.channel = channel;
        this.inFlightRequests = inFlightRequests;
    }

    @Override
    public CompletableFuture<Command> send(Command request) {
        CompletableFuture<Command> completableFuture = new CompletableFuture<>();
        try {
            //在途请求放在inFlightRequests
            inFlightRequests.put(new ResponseFuture(request.getHeader().getRequestId(), completableFuture));
            channel.writeAndFlush(request).addListener((ChannelFutureListener) channelFuture -> {
                //处理发送失败的情况
                if (!channelFuture.isSuccess()) {
                    completableFuture.completeExceptionally(channelFuture.cause());
                    channel.close();
                }
            });
        } catch (Throwable t) {
            //处理发送异常
            inFlightRequests.remove(request.getHeader().getRequestId());
            completableFuture.completeExceptionally(t);

        }
        //构建返回值
        return completableFuture;
    }
}
