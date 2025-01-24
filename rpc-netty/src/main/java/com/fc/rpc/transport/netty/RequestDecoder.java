package com.fc.rpc.transport.netty;

import com.fc.rpc.transport.command.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * request decoder
 *
 * @author since
 * @date 2025-01-02 09:56
 */
public class RequestDecoder extends CommandDecoder {
    @Override
    protected Header decodeHeader(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        return new Header(
                byteBuf.readInt(),
                byteBuf.readInt(),
                byteBuf.readInt()
        );
    }
}
