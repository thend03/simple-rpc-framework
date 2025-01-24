package com.fc.rpc.transport.command;

import java.nio.charset.StandardCharsets;

/**
 * response header
 *
 * @author feng.chuang
 * @date 2024-12-26 08:25
 */
public class ResponseHeader extends Header {
    private int code;
    private String error;

    public ResponseHeader(int requestId, int version, int type, Throwable throwable) {
        this(requestId, version, type, Code.UNKNOWN_ERROR.getCode(), throwable.getMessage());
    }

    public ResponseHeader(int requestId, int version, int type) {
        this(requestId, version, type, Code.SUCCESS.getCode(), null);
    }

    public ResponseHeader(int requestId, int version, int type, int code, String error) {
        super(requestId, version, type);
        this.code = code;
        this.error = error;
    }

    @Override
    public int length() {
        return Integer.BYTES + Integer.BYTES + Integer.BYTES + Integer.BYTES + Integer.BYTES +
                (error == null ? 0 : error.getBytes(StandardCharsets.UTF_8).length);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
