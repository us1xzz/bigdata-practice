package com.xzz.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SparkStreamingDemo1 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkStreamingDemo1")
    //实时数据分析环境,
    val streamingcontext = new StreamingContext(conf,Seconds(3))
    //1.从指定端口采集数据
    val socketDStream: ReceiverInputDStream[String] = streamingcontext.socketTextStream("10.12.18.66",9999)
    //2.从指定文件夹中采集数据,没有flume好用
    val flieStream: DStream[String] = streamingcontext.textFileStream("teststreaming")

    //将采集的数据扁平化
    val wordDStream: DStream[String] = socketDStream.flatMap(_.split(" "))
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
