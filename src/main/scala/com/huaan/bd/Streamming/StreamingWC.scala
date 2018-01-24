package com.huaan.bd.Streamming

import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{HashPartitioner, SparkConf}

object StreamingWC {
  def main(args: Array[String]): Unit = {
    //System.setProperty("hadoop.home.dir", "D:\\software\\learning\\hadoop\\hadoop-2.7.1")
    val conf = new SparkConf().setAppName("Streaming wc")//.setMaster("local[2]")
    val ssc = new StreamingContext(conf, Seconds(5))
    ssc.checkpoint("hdfs://192.168.149.130:9000//user/mai/ck-20180115")

    val dStream = ssc.socketTextStream("192.168.149.130", 8888)
    val tuples = dStream.flatMap(_.split(" ")).map((_, 1))

    // 按批次累加 updateStateByKey
    val res: DStream[(String, Int)] = tuples.updateStateByKey(func, new HashPartitioner(ssc.sparkContext.defaultParallelism), false)

    res.print()
    ssc.start()
    ssc.awaitTermination()
  }

  val func = (iterator: Iterator[(String, Seq[Int], Option[Int])]) => {
    iterator.map(t => {
      (t._1, t._2.sum + t._3.getOrElse(0))
    })
  }

}
