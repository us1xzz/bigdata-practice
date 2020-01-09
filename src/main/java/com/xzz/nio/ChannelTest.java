package com.xzz.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ChannelTest {
    public static void main(String[] args) throws IOException {
        /**
         * 无论是input还是output都可以直接getChannel。
         * 从侧面也说明管道是双向的。
         * channel.read(buffer).可以认为是buffer从channel读
         * channel.write(buffer).可以认为是buffer写入channel
         */
        FileInputStream inputStream = new FileInputStream("Niotest2.txt");
        FileOutputStream outputStream = new FileOutputStream("output.txt");
        FileChannel inputStreamChannel = inputStream.getChannel();
        FileChannel outputStreamChannel = outputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(52);
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
