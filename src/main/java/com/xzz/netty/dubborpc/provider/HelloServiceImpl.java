package com.xzz.netty.dubborpc.provider;

import com.xzz.netty.dubborpc.publicinterface.HelloService;

public class HelloServiceImpl implements HelloService {
    int count = 0;

    @Override
    public String hello(String msg) {
        System.out.println("s收到客户端消息=" + msg);
        if (msg != null) {
            return "你好客户端，我收到你的消息为=" + msg + "第" + (++count) + "次";
        } else {
            return "你好客户端，我已经收到你的消息";
        }
    }
}
