package com.xzz.spark.sql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.expressions.{Aggregator, MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, DoubleType, LongType, StructType}
import org.apache.spark.sql.{DataFrame, Dataset, Encoder, Encoders, Row, SparkSession, TypedColumn}
//用户自定义聚合函数
object SparkSQLUDAFClass {
  def main(args: Array[String]): Unit = {
    //创建配置对象
    val session: SparkSession = SparkSession.builder().master("local[*]").appName("SparkSQLDemo ").config("spark.some.config.option", "some-value").getOrCreate()
    //进行转换之前，需要引入隐式转换规则
    //
    import session.implicits._
    //自定义聚合函数
    //创建聚合函数对象
   val udfa = new MyAgeAvgClassFunction
    //将聚合函数转化为查询列
    val avgCol: TypedColumn[UserBean, Double] = udfa.toColumn.name("avgAge")
    val frame: DataFrame = session.read.json("input/user.json")
    val userDS: Dataset[UserBean] = frame.as[UserBean]
    //应用函数
    userDS.select(avgCol).show()

    session.stop()

  }

}
case class UserBean(name:String,age:BigInt)
case class AvgBuffer(var sum:BigInt,var count:Int)
//声明用户自定义聚合函数(强类型)
//
class MyAgeAvgClassFunction extends Aggregator[UserBean,AvgBuffer,Double]{
  //初始化
  override def zero: AvgBuffer = {
    AvgBuffer(0,0)
  }
  //
  override def reduce(b: AvgBuffer, a: UserBean): AvgBuffer = {
    b.sum =b.sum +a.age
    b.count = b.count +1
    b
  }
  //缓冲区的合并操作
  override def merge(b1: AvgBuffer, b2: AvgBuffer): AvgBuffer = {
    b1.sum = b1.sum +b2.sum
    b1.count =b1.count + b2.count
    b1
  }
  //完成计算
  override def finish(reduction: AvgBuffer): Double = {
    reduction.sum.toDouble/reduction.count
  }

  override def bufferEncoder: Encoder[AvgBuffer] = Encoders.product

  override def outputEncoder: Encoder[Double] = Encoders.scalaDouble
}