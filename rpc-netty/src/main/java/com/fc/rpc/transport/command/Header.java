package com.fc.rpc.transport.command;

/**
 * header
 *
 * @author since
 * @date 2024-12-25 17:59
 */
public class Header {
    private int requestId;
    private int version;
    private int type;

    public Header() {
    }

    public Header(int requestId, int version, int type) {
        this.requestId = requestId;
        this.version = version;
        this.type = type;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int length() {
        return Integer.BYTES + Integer.BYTES + Integer.BYTES;
    }
}
