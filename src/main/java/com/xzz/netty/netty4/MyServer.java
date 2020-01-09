package com.xzz.netty.netty4;

import com.xzz.netty.netty3.ChatServerInitizlizer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 作者：xzz
 * 时间：2019-10-28
 *
 *  * 心跳机制：判断是否还处于连接状态。
 *  * 提问：1.比如说之前的多客户端通信demo，当客户端断开与服务器连接的时候会触发handlerRemoved方法，
 *  * 那么我们就知道该服务的状态了。为什么还需要心跳包去感知呢？
 *  * 真实情况远比我们想象中的复杂，比如我们的客户端是移动手机并且已经建立好了连接，当打开飞行模式（或者强制关机）的时候
 *  * 我们就无法感知当前连接已经断开了（handlerRemoved不会触发的），
  */

public class MyServer {
    public static void main(String[] args) throws  Exception{
        //事件循环组，就是死循环
        //仅仅接受连接，转给workerGroup，自己不做处理
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        //真正处理
        EventLoopGroup workerGroup=new NioEventLoopGroup();
        try{
            //很轻松的启动服务端代码
            ServerBootstrap serverBootstrap=new ServerBootstrap();
            //childHandler子处理器,传入一个初始化器参数ServerInitializer（这里是自定义）
            //TestServerInitializer在channel被注册时，就会创建调用
            serverBootstrap.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class)//使用NioServerSocketChannel.class作为服务器通道的实现
                    .option(ChannelOption.SO_BACKLOG,128)//设置线程队列中等待连接的个数
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new MyServerInitializer());
            //绑定一个端口并且同步，生成一个ChannelFuture对象
            ChannelFuture channelFuture=serverBootstrap.bind(8899).sync();
            //关闭通道，关闭线程组
            channelFuture.channel().closeFuture().sync();
        }finally {
            //循环组优雅关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
