package com.fc.rpc.transport.command;

import java.util.HashMap;
import java.util.Map;

/**
 * code
 *
 * @author since
 * @date 2024-12-26 08:29
 **/
public enum Code {
    SUCCESS(0, "SUCCESS"),
    NO_PROVIDER(-2, "NO_PROVIDER"),
    UNKNOWN_ERROR(-1, "UNKNOWN_ERROR"),
    ;

    private static Map<Integer, Code> codes = new HashMap<>();
    static {
        for (Code code : values()) {
            codes.put(code.code, code);
        }
    }
    private int code;
    private String message;

    Code(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage(Object... args) {
        if (args == null || args.length == 0) {
            return message;
        }
        return String.format(message, args);
    }

    public static Code valueOf(int code) {
        return codes.get(code);
    }
}