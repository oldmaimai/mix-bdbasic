package com.huaan.bd.spark

import org.apache.spark.{SparkConf, SparkContext}

object SparkTest {
  def main(args: Array[String]): Unit = {
    // Local
    val spark = new SparkContext(new SparkConf()
      .setMaster("local").setAppName("Test")
    )
    //spark.setCheckpointDir("/user/mai/input")

    println("-------------Attach debugger now!--------------")
    Thread.sleep(8000)

    // Your job code here, with breakpoints set on the lines you want to pause
    val s = "abc1"
    print(s)
  }

}
