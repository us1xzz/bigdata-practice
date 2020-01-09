package com.xzz.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SparkStreamingKafkaSource {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkStreamingDemo1")
    //实时数据分析环境,
    val streamingcontext = new StreamingContext(conf,Seconds(10))
    //从kafka中关联数据
    val kafkaDstream: ReceiverInputDStream[(String, String)] = KafkaUtils.createStream(
      streamingcontext,
      "localhost:2181",
      "test",
      Map("test20191120" -> 3)
    )
    kafkaDstream
    //将采集的数据扁平化
    val wordDStream: DStream[String] = kafkaDstream.flatMap(_._2.split(" "))
    //将数据结构转变进行统计分析
    val mapDtream: DStream[(String, Int)] = wordDStream.map((_,1))
    //将转换结构后的数据进行聚合处理
    val result: DStream[(String, Int)] = mapDtream.reduceByKey(_+_)
    //打印结果
    result.print()
    //启动采集器
    streamingcontext.start()
    //Driver等待采集的执行
    streamingcontext.awaitTermination()
  }

}
