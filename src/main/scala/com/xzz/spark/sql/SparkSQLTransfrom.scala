package com.xzz.spark.sql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

object SparkSQLTransfrom {
  def main(args: Array[String]): Unit = {
    //创建配置对象
    val session: SparkSession = SparkSession.builder().master("local[*]").appName("SparkSQLDemo ").config("spark.some.config.option", "some-value").getOrCreate()
    val frame: DataFrame = session.read.json("input/user.json")
    //进行转换之前，需要引入隐式转换规则
    //
    import session.implicits._

    //创建RDD
    val dataRDD: RDD[(Int, String, Int)] = session.sparkContext.makeRDD(List((1,"张三",20),(2,"李四",21),(3,"王五",22)))

    //转换为DF
    val df: DataFrame = dataRDD.toDF("id","name","age")
    //转换为DS
    val ds: Dataset[User] = df.as[User]
    //转换为DF
    val df1: DataFrame = ds.toDF()
    //转换为RDD
    val rdd1: RDD[Row] = df1.rdd
    rdd1.foreach{row =>{
      //获取数据时，可以通过索引访问数据
      println(row.getString(1))
    }
    }

    session.stop()

  }

}
case class User(id:Int,name:String,age:Int)