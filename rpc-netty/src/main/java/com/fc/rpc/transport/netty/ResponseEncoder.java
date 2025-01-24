package com.fc.rpc.transport.netty;

import com.fc.rpc.transport.command.Header;
import com.fc.rpc.transport.command.ResponseHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;

/**
 * response encoder
 *
 * @author since
 * @date 2025-01-02 10:03
 */
public class ResponseEncoder extends CommandEncoder {

    @Override
    protected void encodeHeader(ChannelHandlerContext channelHandlerContext, Header header, ByteBuf byteBuf) throws Exception {
        super.encodeHeader(channelHandlerContext, header, byteBuf);
        if (header instanceof ResponseHeader) {
            ResponseHeader responseHeader = (ResponseHeader) header;
            byteBuf.writeInt(responseHeader.getCode());
            int errorLength = header.length() - (Integer.BYTES + Integer.BYTES + Integer.BYTES + Integer.BYTES + Integer.BYTES);
            byteBuf.writeInt(errorLength);
            byteBuf.writeBytes(responseHeader.getError()==null?new byte[0]:responseHeader.getError().getBytes(StandardCharsets.UTF_8));
        } else {
            throw new Exception(String.format("Invalid header type: %s!", header.getClass().getCanonicalName()));
        }
    }
}
