package com.fc.rpc.transport.netty;

import com.fc.rpc.transport.InFlightRequests;
import com.fc.rpc.transport.Transport;
import com.fc.rpc.transport.TransportClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpChannel;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * netty client
 *
 * @author since
 * @date 2025-01-02 09:24
 */
public class NettyClient implements TransportClient {
    private EventLoopGroup ioEventLoopGroup;
    private Bootstrap bootstrap;
    private final InFlightRequests inFlightRequests;
    private List<Channel> channels = new ArrayList<>();

    public NettyClient(InFlightRequests inFlightRequests) {
        this.inFlightRequests = inFlightRequests;
    }

    private Bootstrap newBootstrap(ChannelHandler channelHandler, EventLoopGroup ioEventLoopGroup) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSctpChannel.class)
                .group(ioEventLoopGroup).handler(channelHandler)
                .handler(channelHandler)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        return bootstrap;
    }

    @Override
    public Transport createTransport(SocketAddress address, long connectTimeout) throws InterruptedException, TimeoutException {
        return new NettyTransport(createChannel(address, connectTimeout), inFlightRequests);
    }

    private synchronized Channel createChannel(SocketAddress socketAddress, long connectTimeout) throws InterruptedException, TimeoutException {
        if (socketAddress == null) {
            throw new IllegalArgumentException("socketAddress can not be null");
        }
        if (ioEventLoopGroup == null) {
            ioEventLoopGroup = newIoEventLoopGroup();
        }
        if (bootstrap == null) {
            ChannelHandler channelHandler = newChannelHandlerPipeline();
            bootstrap = newBootstrap(channelHandler, ioEventLoopGroup);
        }
        ChannelFuture channelFuture;
        Channel channel;
        channelFuture = bootstrap.connect(socketAddress);
        if (!channelFuture.await(connectTimeout)) {
            throw new TimeoutException();
        }
        channel = channelFuture.channel();
        channels.add(channel);
        return channel;
    }

    private ChannelHandler newChannelHandlerPipeline() {
        return new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addLast(new ResponseDecoder())
                        .addLast(new RequestEncoder())
                        .addLast(new ResponseInvocation(inFlightRequests));
            }
        };
    }

    private EventLoopGroup newIoEventLoopGroup() {
        if (Epoll.isAvailable()) {
            return new EpollEventLoopGroup();
        } else {
            return new NioEventLoopGroup();
        }
    }

    @Override
    public void close() {
        for (Channel channel : channels) {
            if (null != channel) {
                channel.close();
            }
        }
        if (ioEventLoopGroup != null) {
            ioEventLoopGroup.shutdownGracefully();
        }
        inFlightRequests.close();
    }
}
