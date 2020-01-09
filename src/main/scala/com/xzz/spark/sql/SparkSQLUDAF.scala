package com.xzz.spark.sql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, DoubleType, LongType, StructType}
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
//用户自定义聚合函数
object SparkSQLUDAF {
  def main(args: Array[String]): Unit = {
    //创建配置对象
    val session: SparkSession = SparkSession.builder().master("local[*]").appName("SparkSQLDemo ").config("spark.some.config.option", "some-value").getOrCreate()
    //进行转换之前，需要引入隐式转换规则
    //
    import session.implicits._
    //自定义聚合函数
    //创建聚合函数对象
    val udaf = new MyAgeAvgFunction
    //注册聚合函数
    session.udf.register("avgAge",udaf)
    //使用聚合函数
    val frame: DataFrame = session.read.json("input/user.json")
    frame.createOrReplaceTempView("user")
    session.sql("select avgAge(age) from user").show()


    session.stop()

  }

}
//声明用户自定义聚合函数
//
class MyAgeAvgFunction extends UserDefinedAggregateFunction{
  //输入的数据的结构
  override def inputSchema: StructType = {
    new StructType().add("age",LongType)
  }
  //计算时的数据结构
  override def bufferSchema: StructType = {
    new StructType().add("sum",LongType).add("count",LongType)
  }
  //函数返回数据类型
  override def dataType: DataType = DoubleType
  //函数是否稳定
  override def deterministic: Boolean = true
  //计算之前的换缓冲区的初始化
  override def initialize(buffer: MutableAggregationBuffer): Unit = {
    buffer(0)=0L
    buffer(1)=0L
  }
  //根据查询结果更新缓冲区数据
  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    buffer(0) = buffer.getLong(0)+input.getLong(0)
    buffer(1) = buffer.getLong(1)+1
  }
  //将多个节点的缓冲区合并
  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    //sum累加
    buffer1(0) = buffer1.getLong(0)+buffer2.getLong(0)
    //count累加
    buffer1(1) = buffer1.getLong(1)+buffer2.getLong(1)

  }
  //计算最终结果
  override def evaluate(buffer: Row): Any = {
    buffer.getLong(0).toDouble/buffer.getLong(1)
  }
}
