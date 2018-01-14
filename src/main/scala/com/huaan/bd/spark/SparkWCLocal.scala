package com.huaan.bd.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object SparkWCLocal {
  // args0: hdfs://192.168.149.130:9000/user/mai/input/a.txt
  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "D:\\software\\learning\\hadoop\\hadoop-2.7.1")
//    val conf = new SparkConf().setAppName("sparkwc").setMaster("local[*]")
    val conf = new SparkConf().setAppName("sparkwc").setMaster("local[*]")
    val sc = new SparkContext(conf)

    val lines = sc.textFile(args(0))
    val words = lines.flatMap(_.split(" "))
    val mapped: RDD[(String, Int)] = words.map((_, 1))
    val reduced = mapped.reduceByKey(_ + _)
    val sorted = reduced.sortBy(_._2)
    //reduced.saveAsTextFile("")
    println(sorted.collect().toBuffer)
  }

}
