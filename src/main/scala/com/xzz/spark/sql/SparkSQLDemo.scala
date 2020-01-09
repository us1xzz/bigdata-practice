package com.xzz.spark.sql

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

object SparkSQLDemo {
  def main(args: Array[String]): Unit = {
    val session: SparkSession = SparkSession.builder().master("local[*]").appName("SparkSQLDemo ").config("spark.some.config.option", "some-value").getOrCreate()
    val frame: DataFrame = session.read.json("input/user.json")
    frame.show()
    session.stop()

  }

}
