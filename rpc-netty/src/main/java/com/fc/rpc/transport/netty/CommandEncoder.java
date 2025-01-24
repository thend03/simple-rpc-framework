package com.fc.rpc.transport.netty;

import com.fc.rpc.transport.command.Command;
import com.fc.rpc.transport.command.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * command encoder
 *
 * @author since
 * @date 2025-01-02 08:50
 */
public abstract class CommandEncoder extends MessageToByteEncoder {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object o, ByteBuf byteBuf) throws Exception {
        if (!(o instanceof Command)) {
            throw new Exception(String.format("Unknown type: %s", o.getClass().getCanonicalName()));
        }
        Command command = (Command) o;
        byteBuf.writeInt(Integer.BYTES+command.getHeader().length()+command.getPayload().length);
        encodeHeader(ctx,command.getHeader(),byteBuf);
        byteBuf.writeBytes(command.getPayload());

    }

    protected void encodeHeader(ChannelHandlerContext ctx, Header header, ByteBuf byteBuf) throws Exception {
        byteBuf.writeInt(header.getType());
        byteBuf.writeInt(header.getVersion());
        byteBuf.writeInt(header.getRequestId());
    }
}
