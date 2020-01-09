package com.xzz.spark

import java.util

import org.apache.spark.rdd.RDD
import org.apache.spark.util.AccumulatorV2
import org.apache.spark.{SparkConf, SparkContext}
//自定义累加器
object AccumulaterByMyself {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("AccumulaterByMyself")
    val sc = new SparkContext(conf)
    val dataRDD: RDD[String] = sc.makeRDD(List("hadoop","hive","Spark","Hbase","Scala"))

    //TODO 创建累机器
    val wordAccumulator = new WordAccumulator
    //TODO 注册累加器
    sc.register(wordAccumulator)

    dataRDD.foreach{
      case word =>{
        //TODO 执行累加器的累加功能
        wordAccumulator.add(word)
      }
    }
    //TODO 获取累加器
    println("sum ="+wordAccumulator.value)
  }
}
// 声明累加器
//1.继承AccumulatorV2
//2.实现累加器中的抽象方法
class WordAccumulator extends AccumulatorV2[String ,util.ArrayList[String]] {
  private val list = new util.ArrayList[String]()
  //当前的累加器是不是初始化状态
  override def isZero: Boolean = list.isEmpty
  //复制累加器对象
  override def copy(): AccumulatorV2[String, util.ArrayList[String]] = {
    new WordAccumulator()
  }
  //重置累加器
  override def reset(): Unit = list.clear()
  //向累加器增加数据
  override def add(v: String): Unit = {
    if(v.contains("h")){
      list.add(v)
    }
  }
  //合并累加器
  override def merge(other: AccumulatorV2[String, util.ArrayList[String]]): Unit = {
    list.addAll(other.value)
  }
  //获取累加器的结果
  override def value: util.ArrayList[String] = list
}