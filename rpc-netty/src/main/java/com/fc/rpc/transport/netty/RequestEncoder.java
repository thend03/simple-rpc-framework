package com.fc.rpc.transport.netty;

import com.fc.rpc.transport.command.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * request encoder
 *
 * @author feng.chuang
 * @date 2025-01-02 09:58
 */
public class RequestEncoder extends CommandEncoder {
    @Override
    protected void encodeHeader(ChannelHandlerContext channelHandlerContext, Header header, ByteBuf byteBuf) throws Exception {
        super.encodeHeader(channelHandlerContext, header, byteBuf);
    }
}
