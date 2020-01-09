import scala.io.{BufferedSource, Source}
object binarytransform {



  def main(args: Array[String]): Unit = {
    val source: BufferedSource = Source.fromFile("D:\\idealproject2\\bigdata-practice\\input\\TCPmessage")
    val lines: Iterator[String] = source.getLines()

    val message: Iterator[String] = lines.map(_.split(":")(1))
    val byteses: Iterator[Array[Byte]] = message.map(StringHexUtil.StringToBytes(_))
    val ints: Iterator[Int] = byteses.map(ByteUtil.getInt(_,14))
    ints.foreach(println)



  }

}
