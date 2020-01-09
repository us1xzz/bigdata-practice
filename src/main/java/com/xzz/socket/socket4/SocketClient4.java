package com.xzz.socket.socket4;
/*作者：xzz
  时间：2019-10-24
  描述：实现Java的socket服务,并且可以实现多个客户端与服务端进行通信，主要采用多线程
*/
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient4 {
    public static void main(String[] args) throws Exception{
        try {
            Socket socket = new Socket("localhost",8088);
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            Scanner scanner = new Scanner(System.in);
            if(scanner.hasNext()){
                String str = scanner.next();
                int type = 1;
                byte[] data = str.getBytes();
                int len = data.length + 5;
                dataOutputStream.writeByte(type);
                dataOutputStream.writeInt(len);
                dataOutputStream.write(data);
                dataOutputStream.flush();
            }


        }catch (IOException e){
            e.printStackTrace();
        }



    }
}
