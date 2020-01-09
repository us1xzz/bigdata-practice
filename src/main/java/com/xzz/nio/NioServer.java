package com.xzz.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class NioServer {
    private  static Map<String, SocketChannel> clientMap=new HashMap<>();
    public static void main(String[] args) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(8899));
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while(true){
            try {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                selectionKeys.forEach(selectionKey -> {
                    final SocketChannel client;
                    try {
                        if (selectionKey.isAcceptable()){
                            ServerSocketChannel serverSocketChannel1 =(ServerSocketChannel) selectionKey.channel();
                            client = serverSocketChannel1.accept();
                            client.configureBlocking(false);
                            client.register(selector,SelectionKey.OP_READ);
                            String key ="["+ UUID.randomUUID().toString()+"]";
                            clientMap.put(key,client);

                        }
                        else if(selectionKey.isReadable()){
                            client = (SocketChannel)selectionKey.channel();
                            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                            int count = client.read(readBuffer);
                            if (count>0){
                                readBuffer.flip();
                                Charset charset = Charset.forName("utf-8");
                                String reciveMassage = String.valueOf(charset.decode(readBuffer).array());
                                System.out.println(client+":"+reciveMassage);
                                String senderKey=null;
                                for(Map.Entry<String ,SocketChannel> entry:clientMap.entrySet()){
                                    if(client == entry.getValue()){
                                        senderKey = entry.getKey();
                                        break;

                                    }
                                }
                                for(Map.Entry<String,SocketChannel> entry: clientMap.entrySet()){
                                    SocketChannel socketChannel=entry.getValue();
                                    ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                                    writeBuffer.put((senderKey+":"+reciveMassage).getBytes());
                                    writeBuffer.flip();
                                    socketChannel.write(writeBuffer);
                                }
                            }

                        }


                    }catch (Exception e){
                        e.printStackTrace();
                    }

                });
                //将key清空，相当于Itetor一次性remove
                selectionKeys.clear();

            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }
}
