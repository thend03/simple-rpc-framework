package com.fc;

/**
 *
 *接口实现类
 * @author since
 * @date 2024-12-17 16:56
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "hello " + name;
    }
}
