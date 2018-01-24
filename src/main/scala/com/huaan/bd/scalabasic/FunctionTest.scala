package com.huaan.bd.scalabasic

import scala.math._
object FunctionTest {
  def main(args: Array[String]): Unit = {
    val num = 3.14
    val fun = ceil _
    Array(3.14, 1.42, 2.0).map(fun).foreach(println)

  }
}
