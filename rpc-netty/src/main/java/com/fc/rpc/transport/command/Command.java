package com.fc.rpc.transport.command;

/**
 * command
 *
 * @author since
 * @date 2024-12-25 17:57
 */
public class Command {
    protected  Header header;

    private byte[] payload;

    public Command(Header header, byte[] payload) {
        this.header = header;
        this.payload = payload;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }


}
