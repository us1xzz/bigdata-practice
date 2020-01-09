package com.xzz.socket.socket2;
/*作者：xzz
  时间：2019-10-24
  描述：实现Java的socket服务,并且可以实现多个客户端与服务端进行通信，主要采用多线程
*/
import java.io.*;
import java.net.Socket;

public class SocketClient2 {
    public static void main(String[] args) throws Exception{
        try {
            Socket socket = new Socket("localhost",8088);
            //通过socket获取字符流
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //通过标椎输入流获取字符流
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
            //通过while循环实现输入
            while(true){
                String str = bufferedReader.readLine();
                bufferedWriter.write(str);
                bufferedWriter.write("\n");

                bufferedWriter.flush();
                //刷新输入流

            }


        }catch (IOException e){
            e.printStackTrace();
        }



    }
}
