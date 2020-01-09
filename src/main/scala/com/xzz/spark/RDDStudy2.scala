package com.xzz.spark
//mapPartitons and mapPartitonsWithIndex
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object RDDStudy2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("WordCount")
    val sc = new SparkContext(conf)
    val listRDD: RDD[Int] = sc.makeRDD(1 to 10,2)
    //mapPartitons可以对一个RDD中所有的分区进行遍历
    //mapPartitons效率优于map算子，减少了发送到执行器执行交互的次数
    //mapPartitons可能会出现内存溢出（OOM）
    //val mapPartitionRDD: RDD[Int] = listRDD.mapPartitions(datas =>{datas.map(data=>data*2)})
    //
    val tupleRDD: RDD[(Int, String)] = listRDD.mapPartitionsWithIndex {
      case (num, datas) => {
        datas.map((_, "分区号：" + num))
      }
    }

    tupleRDD.collect().foreach(println)
  }



}
