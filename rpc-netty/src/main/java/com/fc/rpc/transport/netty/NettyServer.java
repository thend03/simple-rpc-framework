package com.fc.rpc.transport.netty;

import com.fc.rpc.transport.RequestHandlerRegistry;
import com.fc.rpc.transport.TransportServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * netty server
 *
 * @author since
 * @date 2025-01-03 11:18
 */
public class NettyServer implements TransportServer {
    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private int port;

    private EventLoopGroup acceptEventGroup;

    private EventLoopGroup ioEventGroup;

    private Channel channel;

    private RequestHandlerRegistry requestHandlerRegistry;

    @Override
    public void start(RequestHandlerRegistry requestHandlerRegistry, int port) throws Exception {
        this.port = port;
        this.requestHandlerRegistry = requestHandlerRegistry;
        this.acceptEventGroup = new NioEventLoopGroup();
        this.ioEventGroup = new NioEventLoopGroup();
        ioEventGroup = new NioEventLoopGroup();
        ChannelHandler channelHandler = newChannelHandlerPipeline();
        ServerBootstrap bootstrap = newServerBootstrap(channelHandler,acceptEventGroup,ioEventGroup);
        this.channel = doBind(bootstrap);
    }

    @Override
    public void stop() {
        if (acceptEventGroup != null) {
            acceptEventGroup.shutdownGracefully();
        }
        if (ioEventGroup != null) {
            ioEventGroup.shutdownGracefully();
        }
        if (channel != null) {
            channel.close();
        }
    }

    private Channel doBind(ServerBootstrap serverBootstrap) throws Exception {
        return serverBootstrap.bind(port).sync().channel();
    }

    private EventLoopGroup newEventLoopGroup() {
        if (Epoll.isAvailable()) {
            return new EpollEventLoopGroup();
        } else {
            return new NioEventLoopGroup();
        }
    }

    private ChannelHandler newChannelHandlerPipeline() {
        return new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                channel.pipeline()
                        .addLast(new RequestDecoder())
                        .addLast(new ResponseEncoder())
                        .addLast(new RequestInvocation(requestHandlerRegistry));
            }
        };
    }

    private ServerBootstrap newServerBootstrap(ChannelHandler channelHandler, EventLoopGroup eventLoopGroup, EventLoopGroup ioEventGroup) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.channel((Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class))
                .group(acceptEventGroup, this.ioEventGroup)
                .childHandler(channelHandler)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        return bootstrap;
    }
}
