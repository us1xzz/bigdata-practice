package com.xzz.netty.netty3;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
/**
 * 作者：xzz
 * 时间：2019-10-28
 * 多个客户端连接服务端,可以相互发送消息
 * 并且有上下线通知
 */
public class ChatServer {
    public static void main(String[] args) throws  Exception{
        //事件循环组，就是死循环
        //仅仅接受连接，转给workerGroup，自己不做处理
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        //真正处理
        EventLoopGroup workerGroup=new NioEventLoopGroup();
        try{
            //很轻松的启动服务端代码
            ServerBootstrap serverBootstrap=new ServerBootstrap();//服务器启动引导类
            //childHandler子处理器,传入一个初始化器参数ServerInitializer（这里是自定义）
            //TestServerInitializer在channel被注册时，就会创建调用
            serverBootstrap.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class).
                    childHandler(new ChatServerInitizlizer());
            //绑定一个端口并且同步，生成一个ChannelFuture对象
            ChannelFuture channelFuture=serverBootstrap.bind(8899).sync();
            //对关闭的监听
            channelFuture.channel().closeFuture().sync();
        }finally {
            //循环组优雅关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
