package com.xzz.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
//NIO模式读取文件的中的信息
public class NioTest2 {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("Niotest2.txt");
        FileChannel fileChannel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        fileChannel.read(byteBuffer);
        byteBuffer.flip();
        while(byteBuffer.remaining()>0){
            byte b = byteBuffer.get();
            System.out.println("Character:"+(char)b);
        }
        fileInputStream.close();
    }
}
