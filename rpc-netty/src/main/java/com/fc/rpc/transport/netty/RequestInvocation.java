package com.fc.rpc.transport.netty;

import com.fc.rpc.transport.RequestHandler;
import com.fc.rpc.transport.RequestHandlerRegistry;
import com.fc.rpc.transport.command.Command;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * request invocation
 *
 * @author since
 * @date 2025-01-03 11:28
 */@ChannelHandler.Sharable
public class RequestInvocation extends SimpleChannelInboundHandler<Command> {
     private static final Logger logger = LoggerFactory.getLogger(RequestInvocation.class);
     private final RequestHandlerRegistry requestHandlerRegistry;

    public RequestInvocation(RequestHandlerRegistry requestHandlerRegistry) {
        this.requestHandlerRegistry = requestHandlerRegistry;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Command request) throws Exception {
        RequestHandler requestHandler = requestHandlerRegistry.get(request.getHeader().getType());


    }
}
