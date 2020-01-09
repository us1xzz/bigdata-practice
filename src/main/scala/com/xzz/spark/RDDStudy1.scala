package com.xzz.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object RDDStudy1 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("WordCount")
    val sc = new SparkContext(conf)
    val lisrRDD: RDD[Int] = sc.parallelize(List(1,2,3,4,5,6))
    lisrRDD.saveAsTextFile("ouput")
  }

}
