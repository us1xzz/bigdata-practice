package com.xzz.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object WordCount {
  def main(args: Array[String]): Unit = {
    //创建上下文对象
    //app id 全局应用名称
    val conf = new SparkConf().setMaster("local[*]").setAppName("WordCount")
    val sc = new SparkContext(conf)
    //读取文件。一行一行的读
    val lines: RDD[String] = sc.textFile("D:\\idealproject2\\bigdata-practice\\input")
    val words: RDD[String] = lines.flatMap(_.split(" "))
    val wordAndone: RDD[(String, Int)] = words.map((_,1))
    val wordTosum: RDD[(String, Int)] = wordAndone.reduceByKey(_+_)
    val result: Array[(String, Int)] = wordTosum.collect()
    result.foreach(println)









  }



}
