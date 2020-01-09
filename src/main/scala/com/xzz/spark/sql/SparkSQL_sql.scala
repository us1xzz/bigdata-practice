package com.xzz.spark.sql

import org.apache.spark.sql.{DataFrame, SparkSession}

object SparkSQL_sql {
  def main(args: Array[String]): Unit = {
    //创建配置对象
    val session: SparkSession = SparkSession.builder().master("local[*]").appName("SparkSQLDemo ").config("spark.some.config.option", "some-value").getOrCreate()
    val frame: DataFrame = session.read.json("input/user.json")
    //将DataFram转换成为一张表
    frame.createTempView("user")
    //采用sql的方式访问数据
    session.sql("select * from user").show()
    //frame.show()
    session.stop()

  }

}
