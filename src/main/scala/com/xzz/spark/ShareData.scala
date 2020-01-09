package com.xzz.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext, util}

object ShareData {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("ShareData")
    val sc = new SparkContext(conf)
    val dataRDD: RDD[Int] = sc.makeRDD(List(1,2,3,4,5),2)
    var sum = 0
    //使用累加器共享变量。来累加数据
    //创建累加器对象
    val accumulator: util.LongAccumulator = sc.longAccumulator

    dataRDD.foreach{
      case i =>{
        //执行累加器的累加功能
        accumulator.add(i)
      }
    }
    //获取累加器的值
    println(accumulator.value)


  }
}
