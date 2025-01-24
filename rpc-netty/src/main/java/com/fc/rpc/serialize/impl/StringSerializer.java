package com.fc.rpc.serialize.impl;

import com.fc.rpc.serialize.Serializer;
import com.fc.rpc.serialize.Types;

import java.nio.charset.StandardCharsets;

/**
 * string serializer
 *
 * @author feng.chuang
 * @date 2024-12-20 09:01
 */
public class StringSerializer implements Serializer<String> {
    @Override
    public int size(String entry) {
        return entry.getBytes(StandardCharsets.UTF_8).length;
    }

    @Override
    public void serialize(String entry, byte[] bytes, int offset, int length) {
        byte[] strBytes = entry.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(strBytes, 0, bytes, offset, strBytes.length);
    }

    @Override
    public String parse(byte[] bytes, int offset, int length) {
        return new String(bytes, offset, length, StandardCharsets.UTF_8);
    }

    @Override
    public byte type() {
        return Types.TYPE_STRING;
    }

    @Override
    public Class<String> getSerializeClass() {
        return String.class;
    }
}
