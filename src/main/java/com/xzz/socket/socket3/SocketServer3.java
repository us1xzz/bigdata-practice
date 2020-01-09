package com.xzz.socket.socket3;
/*作者：xzz
  时间：2019-10-24
  描述：实现Java的socket服务,并且可以实现多个客户端与服务端进行通信，采用线程池的概念
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.beans.Encoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer3 {
    public static void main(String[] args) throws IOException{

        //初始化服务端socket并且绑定8088端口
        ServerSocket serverSocket = new ServerSocket(8088);
        //创建一个线程池
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        while(true){
            final Socket socket = serverSocket.accept();
            Runnable runnable = ()->{
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                    //读取一行数据
                    String str;
                    //通过while循环不断读取信息
                    while((str = bufferedReader.readLine())!=null){
                        //输出打印
                        System.out.println("客户端说："+ str);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            };
            executorService.submit(runnable);
        }


        }
    }

