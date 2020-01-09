package com.xzz.socket.socket4;
/*作者：xzz
  时间：2019-10-24
  描述：实现Java的socket服务,并且可以实现多个客户端与服务端进行通信，采用线程池的概念
  发送接受定长数据,包类型+包长度+消息内容定义一个socket通信对象，数据类型为byte类型，包长度为int类型，消息内容为byte类型。
*/



import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ServiceConfigurationError;

public class SocketServer4 {
    public static void main(String[] args) throws Exception{
        try {
            //初始化服务端socket并且绑定8088端口
            ServerSocket serverSocket = new ServerSocket(8088);
            Socket client = serverSocket.accept();
            InputStream inputStream = client.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            while(true){
                byte b = dataInputStream.readByte();
                int len = dataInputStream.readInt();
                byte[] data = new byte[len - 5];
                dataInputStream.readFully(data);
                //String str = data.toString();
                String str = new String(data);
                System.out.println("获取数据类型为"+b);
                System.out.println("获取数据长度为："+len);
                System.out.println("获取数据内容为："+str);

            }


        }catch (IOException e){
            e.printStackTrace();
        }


    }
}
