package com.huaan.bd.scalabasic

/**
  * 集合操作
  */
object CollectionTest {
  def main(args: Array[String]): Unit = {
    val list0 = List(8, 3, 4, 1, 2, 6, 9, 7, 5, 0)
    // 每个元素乘以2生成新的集合
    val list1 = list0.map(_ * 2)
    println(list1)
    // 偶数
    val list1a = list0.filter(_ % 2 == 0)
    println(list1a)
    // 排序
    val list2 = list0.sorted
    println(list2)
    //  反转排序
    val list3 = list2.reverse
    println(list3)
    // 将list0中的元素每4个一组
    val iterator = list0.grouped(4)
    //    println(iterator.toBuffer)
    // 将iterator转化成list
    val list4 = iterator.toList
    println(list4)
    // 把多个list压扁成一个list
    val list5 = list4.flatten
    println(list5)

    // 先用空格分隔，再压平
    val lines = List("hello java hell scala", "hello scala", "hello python")
    /*  方法1： 先变成List(String[], String[]), 再用flatten把List里面的内容压平
    val words = lines.map(_.split(" ")) // 返回一个list，list里面是String[]
    println(words)
    val flatWords = words.flatten
    println(flatWords)
    */
    // 方法2： flatMap = map + flatten
    val flatWords = lines.flatMap(_.split(" "))
    println(flatWords)

    // 下面看下聚合（reduce）
    val arr = Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val res = arr.sum
    println(res)
    // 并行计算求和, 可能 (1,2,3,4) + (5,6,7,8) + (9,10)
    val res2 = arr.par.sum
    println(res2)
    // 按特定顺序聚合 ((1+2)+3)+4...
    val res3 = arr.reduce(_ + _)
    println(res3)
    val res4 = arr.reduceLeft(_ + _)
    println(res4)
    // 并行方式进行聚合
    val res5 = arr.par.reduce(_ + _)
    print(res5)


  }
}
