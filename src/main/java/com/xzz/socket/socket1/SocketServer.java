package com.xzz.socket.socket1;
/*作者：xzz
  时间：2019-10-24
  描述：实现Java的socket服务
*/
import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStreamReader;

import java.net.ServerSocket;

import java.net.Socket;

public class SocketServer {
    public static void main(String[] args) throws Exception{
        try {
            //监听指定端口
            ServerSocket serverSocket = new ServerSocket(8088);
            //server将一直等待连接的到来
            System.out.println("将一直等待连接的到来");
            Socket socket = serverSocket.accept();
            //获取输入流并且指定统一的编码格式
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));

            String str;
            //通过while循环不断读取信息
            while((str = bufferedReader.readLine())!=null){
                //输出打印
                System.out.println(str);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
