package com.xzz.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.Future;

public class Producer {
    public static void main(String[] args) throws InterruptedException {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        //ack是判断请求是否为完整的条件（即判断是否成功发送）。all将会阻塞消息，这种设置性能最低，但是最可靠。
        props.put("acks", "1");
        //retries,如果请求失败，生产者会自动重试，我们指定是0次，如果启用重试，则会有重复消息的可能性。
        props.put("retries", 0);
        //producer缓存每个分区未发送消息，缓存的大小是通过batch.size()配置设定的。值较大的话将会产生更大的批。并需要更多的内存(因为每个“活跃”的分区都有一个缓冲区)
        props.put("batch.size", 16384);
        //默认缓冲区可立即发送，即便缓冲区空间没有满；但是，如果你想减少请求的数量，可以设置linger.ms大于0.这将指示生产者发送请求之前等待一段时间
        //希望更多的消息补填到未满的批中。这类似于tcp的算法，例如上面的代码段，可能100条消息在一个请求发送，因为我们设置了linger时间为1ms，然后，如果我们
        //没有填满缓冲区，这个设置将增加1ms的延迟请求以等待更多的消息。需要注意的是，在高负载下，相近的时间一般也会组成批，即使是linger.ms=0。
        //不处于高负载的情况下，如果设置比0大，以少量的延迟代价换取更少的，更有效的请求。
        props.put("linger.ms", 1);
        //buffer.memory控制生产者可用的缓存总量，如果消息发送速度比其传输到服务器的快，将会耗尽这个缓存空间。当缓存空间耗尽，其他发送调用将被阻塞，阻塞时间的阈值
        //通过max.block.ms设定，之后他将抛出一个TimeoutExecption。
        props.put("buffer.memory", 33554432);
        //key.serializer和value.serializer示例：将用户提供的key和value对象ProducerRecord转换成字节，你可以使用附带的ByteArraySerizlizaer或StringSerializer处理简单的byte和String类型.
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        //设置kafka的分区数量
        props.put("kafka.partitions", 1);
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);
        for(int i = 1;i<100;i++){
            System.out.println("key"+i+",value"+i);
            producer.send(new ProducerRecord<String, String>("test20191120", "keyxzz" + i, "valuexzz" + i));
            Thread.sleep(100);


        }

        producer.close();

    }

}
