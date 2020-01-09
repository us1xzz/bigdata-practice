package com.xzz.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
/*allocateDirect直接在内存，不在jvm的堆内存里*/
public class Niotest4 {
    public static void main(String[] args) throws IOException {

        FileInputStream inputStream = new FileInputStream("Niotest2.txt");
        FileOutputStream outputStream = new FileOutputStream("output.txt");
        FileChannel inputStreamChannel = inputStream.getChannel();
        FileChannel outputStreamChannel = outputStream.getChannel();
        //allocateDirect直接在内存，不在jvm的堆内存里
        ByteBuffer buffer = ByteBuffer.allocateDirect(52);
        while(true){
            buffer.clear(); //注释掉会发生什么？
            int read = inputStreamChannel.read(buffer);
            System.out.println("read:"+read);
            if (read == -1){
                break;
            }
            buffer.flip();
            outputStreamChannel.write(buffer);

        }
        inputStreamChannel.close();
        outputStreamChannel.close();


    }
}
