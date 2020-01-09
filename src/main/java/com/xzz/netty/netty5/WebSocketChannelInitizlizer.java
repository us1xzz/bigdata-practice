package com.xzz.netty.netty5;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketChannelInitizlizer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //因为是基于Http协议的，所以使用http编解码器
        pipeline.addLast(new HttpServerCodec());
        //以块的方式写进去
        pipeline.addLast(new ChunkedWriteHandler());
        /**
         *  特别重要，http数据在传输过程是分段的
         *  HttpObjectAggregator,而他就是将多个段聚合起来。
         */
        pipeline.addLast(new HttpObjectAggregator(8192));
        /**
         * 对于webSocket，他的数据传输是以frame（帧）的形式传递
         * 可以查看WebSocketFrame，他有六个子类
         * /ws,表示的websocket的地址,即后边的ws。ws://localhost/ws
         */
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        pipeline.addLast(new TextWebSocketFrameHandler());

    }
}
