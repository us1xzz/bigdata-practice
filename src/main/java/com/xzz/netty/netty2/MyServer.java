package com.xzz.netty.netty2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**/
public class MyServer {
    public static void main(String[] args) throws Exception{
        //仅仅接受连接，赋一些初值，转给workerGroup，自己不做处理,就是死循环，不停的执行
        //线程的个数取决于CPU的超线程核心数，比如八核16线程
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            //设置初始值
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new MyServerInitializer());//到此，serverBootstrap的成员变量都设置好了
            ChannelFuture channelFuture = serverBootstrap.bind(10005).sync();//bind方法开始执行服务器相关操作
            //ChannelFuture各种异步操作中各种状态的处理
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

        }
    }
}
