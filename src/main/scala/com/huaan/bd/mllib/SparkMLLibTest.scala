package com.huaan.bd.mllib

import org.apache.spark.mllib.linalg
import org.apache.spark.mllib.linalg.distributed.RowMatrix
import org.apache.spark.mllib.linalg.{Matrices, Vectors}
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.stat.Statistics
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.{SparkConf, SparkContext}

object SparkMLLibTest {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("SparkTest example")
    val sc = new SparkContext(conf)
    // 1. 测试RDD的基本API
    //rddApiTest(sc)
    // 2. mllib基本概念
    mllibBasicTest(sc)


  }

  private def mllibBasicTest(sc: SparkContext) = {
    println()
    val vd: linalg.Vector = Vectors.dense(2, 0, 6)
    println(vd(2))
    // 向量标签
    val pos = LabeledPoint(1, vd)
    println(pos.features)
    println(pos.label)

    val vs: linalg.Vector = Vectors.sparse(4, Array(0, 1, 2, 3), Array(9, 5, 0, 7))
    println(vs(2))
    val neg = LabeledPoint(2, vs)
    println(neg.features)
    println(neg.label)

    println("从文件中创建向量标签")
    val mu = MLUtils.loadLibSVMFile(sc, "E:\\learning\\github\\mix-bdbasic\\files\\loadLibSVMFile.txt")
    mu.foreach(println)

    println("本地矩阵")
    val mx = Matrices.dense(2, 3, Array(1, 2, 3, 4, 5, 6))
    println(mx)

    println("行矩阵")
    val rdd = sc.textFile("E:\\learning\\github\\mix-bdbasic\\files\\RowMatrix.txt")
      .map(_.split(" ").map(_.toDouble))
      .map(line => Vectors.dense(line))// 每一行转化为Vector格式
    val rm = new RowMatrix(rdd)
    println(rm)
    println(rm.numRows())
    println(rm.numCols())

    println("=============数理统计===============")
    val rdd2 = sc.textFile("E:\\learning\\github\\mix-bdbasic\\files\\testSummary.txt")
      .map(_.split(" ").map(_.toDouble))
      .map(line => Vectors.dense(line))// 每一行转化为Vector格式
    val summary = Statistics.colStats(rdd2)
    println(summary.mean)
    println(summary.variance)
    println(summary.normL1)
    println(summary.normL2)

    println("=============两组数据的相关系数============")
    val corrX = sc.textFile("E:\\learning\\github\\mix-bdbasic\\files\\testCorrectX.txt")
      .flatMap(_.split(' ').map(_.toDouble))
    val corrY = sc.textFile("E:\\learning\\github\\mix-bdbasic\\files\\testCorrectY.txt")
      .flatMap(_.split(' ').map(_.toDouble))
    val correlation = Statistics.corr(corrX, corrY)
    println(correlation)

    println("==============分层抽样===============")
    // 原始数据转化成对应的map，[数据，标签]
    val data = sc.textFile("E:\\learning\\github\\mix-bdbasic\\files\\testStratifiedSampling.txt")
      .map(row => {
        if (row.length == 3)
          {
            (row, 1)
          }
        else
          {
            (row, 2)
          }
      })
    // 下面进行抽样
    // 设定抽样格式
//    val fractions =  (List(("aa", 0.2))).toMap
//    val approxSample = data.sampleBy
    // Key(withReplacement = false, fractions, 0)
//    approxSample.foreach(println)

    println("卡方校验")
    val vd1 = Vectors.dense(1, 2, 3, 4, 5)
    println(Statistics.chiSqTest(vd1))

  }


  private def rddApiTest(sc: SparkContext) = {
    val arr = sc.parallelize(Array(1, 2, 3, 4, 5, 6))
    // 1. aggregate 第一个为初始值，第二个参数为每个分区的计算结果，第三个参数为不同分区的组合结果
    val result = arr.aggregate(0)(Math.max(_, _), _ + _)
    println(result)

    val arr2 = sc.parallelize(Array(1, 2, 3, 4, 5, 6), 2)
    val result2 = arr2.aggregate(0)(Math.max(_, _), _ + _)
    println(result2)

    val arr3 = sc.parallelize(Array("ab", "cc", "a", "dd"))
    val result3 = arr3.aggregate("")(_ + _, _ + _)
    println(result3)
    // 2. cache 保存数据内容
    println(arr3)
    println(arr3.cache())
    // 3. 未进行action操作时，可以迭代形式打印数据
    arr3.foreach(println)
    // 4. cartesian笛卡尔积
    val arr4 = sc.parallelize(Array(6, 5, 4, 3, 2, 1))
    val result4 = arr.cartesian(arr4)
    result4.foreach(print)
    // 5. 数据重新分片, val arr = sc.parallelize(Array(1, 2, 3, 4, 5, 6))
    val arr5 = arr.coalesce(2, true)
    println("======== 数据重新分片 ============")
    println(arr.aggregate(0)(Math.max(_, _), _ + _))
    println(arr5.aggregate(0)(Math.max(_, _), _ + _))
    // 5.1 repartition 也是数据重分布
    val arr5_1 = arr.repartition(3)
    println("分区数：" + arr5_1.partitions.length)
    // 6. 以value计算countByValue : 计算数据集中某个数据出现的个数，并以map的方式返回
    val result6 = arr.countByValue()
    println("以value计算countByValue:" + result6)
    result6.foreach(print)
    // 7. 计算key出现的个数countByKey
    val arr7 = sc.parallelize(Array((1, "cool"), (2, "good"), (1, "bad"), (1, "fine")))
    val result7 = arr7.countByKey()
    result7.foreach(print)
    // 8. distinct 去重
    val arr8 = sc.parallelize(Array("cool", "good", "good", "find"))
    arr8.distinct.foreach(println)
    // 9. filter
    val result9 = arr.filter(_ >= 3)
    result9.foreach(println)
    // 10. map 和 flatMap的对比，flatMap是把map的计算结果打开后首尾连接起来
    //val arr = sc.parallelize(Array(1, 2, 3, 4, 5, 6))
    val result10 = arr.map(x => List(x + 1)).collect()
    result10.foreach(print)
    println()
    val result10_1 = arr.flatMap(x => List(x + 1)).collect()
    result10_1.foreach(print)
    println
    // 11. groupBy 根据条件分组
    println(" groupBy 根据条件分组")

    // 定义分组条件
    def myFilter(num: Int): Unit = {
      num >= 3
    }

    val arr11 = sc.parallelize(Array(1, 2, 3, 4, 5, 6))
    val result11 = arr11.groupBy(myFilter(_), 1)
    result11.foreach(print)
    // 12. keyBy 为每个数据增加一个key
    val str = sc.parallelize(Array("one", "two", "three", "four", "five"))
    val result12 = str.keyBy(word => word.size)
    result12.foreach(print)
    // 13. reduce对两个数据进行拟合(合并处理)
    println
    val result13 = str.reduce(_ + _)
    result13.foreach(print)
    println

    // 13.1 reduce 寻找最长字符串
    def myFun(str1: String, str2: String): String = {
      if (str1.size > str2.size) {
        return str1
      }
      return str2
    }

    val result13_1 = str.reduce(myFun)
    result13_1.foreach(print)
    println
    // 14. sortBy 排序
    // 创建包含元组的list，._1表示元组的第一个元素
    val str14 = sc.parallelize(Array((5, "b"), (6, "a"), (1, "f"), (3, "d"), (4, "c"), (2, "e")))
    val result14 = str14.sortBy(_._1, true)
    val result14_1 = str14.sortBy(_._2, false)
    result14.foreach(print)
    println
    result14_1.foreach(print)
    // 15. zip 合并压缩
    val arr15 = Array(1, 2, 3, 4)
    val arr15_1 = Array("a", "b", "c", "d")
    val arr15_2 = Array("e", "f", "g", "h")
    val result15 = arr15.zip(arr15_1)
    val result15_1 = arr15.zip(arr15_1).zip(arr15_2)
    println()
    result15.foreach(print)
    println
    result15_1.foreach(print)
  }
}
