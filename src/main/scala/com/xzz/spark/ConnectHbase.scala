package com.xzz.spark

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration, HColumnDescriptor, HTableDescriptor, TableName}
import org.apache.hadoop.hbase.client.{HBaseAdmin, Put, Result}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapred.JobConf
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}



object ConnectHbase {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("HbaseConnect")
    val sc = new SparkContext(conf)
    //构建HBase配置信息
    val configuration: Configuration = HBaseConfiguration.create()
   /* configuration.set(TableInputFormat.INPUT_TABLE,"t_user")
    //从HBase读取数据形成RDD
    val hbaseRDD: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(configuration,classOf[TableInputFormat],classOf[ImmutableBytesWritable],classOf[Result])
    //对hbaseRDD进行处理
    hbaseRDD.foreach{
      case(rowkey,result) =>{
          val cells: Array[Cell] = result.rawCells()
        for (cell <- cells) {
          println(Bytes.toString(CellUtil.cloneValue(cell)))
        }
      }
    }
    */
   //构建Hbase表描述器
    val userTable = TableName.valueOf("usertable")
    val tableDescr = new HTableDescriptor(userTable)
    tableDescr.addFamily(new HColumnDescriptor("info".getBytes))
    val admin = new HBaseAdmin(configuration)
    if (admin.tableExists(userTable)) {
      admin.disableTable(userTable)
      admin.deleteTable(userTable)
    }
    admin.createTable(tableDescr)
    val nameRDD: RDD[(String, String, Int)] = sc.makeRDD(List(("1002","zhangsan",13),("1003","lisi",14),("1004","wangwu",16)))
    val hbasePut: RDD[(ImmutableBytesWritable, Put)] = nameRDD.map {
      case (rowkey, name, age) => {
        val put = new Put(Bytes.toBytes(rowkey))
        put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("name"), Bytes.toBytes(name))
        put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("age"), Bytes.toBytes(age))
        (new ImmutableBytesWritable(Bytes.toBytes(rowkey)), put)
      }
    }
    val jobConf = new JobConf(configuration)
    jobConf.setOutputFormat(classOf[TableOutputFormat])
    jobConf.set(TableOutputFormat.OUTPUT_TABLE, "usertable")
    hbasePut.saveAsHadoopDataset(jobConf)

    sc.stop()
  }

}
