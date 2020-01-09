
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}


object  MessageCountByTrainNumber {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("TrainMessageCount")
    val sc = new SparkContext(conf)
    //读取文件。一行一行的读
    val lines: RDD[String] = sc.textFile("C:\\Users\\test\\Desktop\\TCPpackage")
    val message: RDD[String] = lines.map(_.split(":")(1))

    val Byte_message: RDD[Array[Byte]] = message.map(StringHexUtil.StringToBytes(_))
    val train_num: RDD[Int] = Byte_message.map(ByteUtil.getInt(_,14))
    val train_numAndOne: RDD[(Int, Int)] = train_num.map((_,1))
    val result: RDD[(Int, Int)] = train_numAndOne.reduceByKey(_+_)
    result.foreach(println)

  }

}


