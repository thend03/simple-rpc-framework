package com.fc.rpc.transport;

import com.fc.rpc.transport.command.Command;

/**
 * request handler
 *
 * @author since
 * @date 2025-01-02 09:07
 */
public interface RequestHandler {
    /**
     * 处理请求
     * @param requestCommand request command
     * @return command
     */
    Command handle(Command requestCommand);

    /**
     * 支持的请求类型
     * @return type
     */
    int type();
}
