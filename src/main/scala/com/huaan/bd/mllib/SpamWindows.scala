package com.huaan.bd.mllib

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.classification.LogisticRegressionWithSGD
import org.apache.spark.mllib.feature.HashingTF
import org.apache.spark.mllib.regression.LabeledPoint

object SpamWindows {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("Spam example")
    //.setMaster("local[*]")
    val sc = new SparkContext(conf)
    val spam = sc.textFile("E:\\learning\\github\\mix-bdbasic\\files\\spam.txt")
    val normal = sc.textFile("E:\\learning\\github\\mix-bdbasic\\files\\ham.txt")

    val tf = new HashingTF(numFeatures = 10000)
    val spamFeatures = spam.map(email => tf.transform(email.split(" ")))
    val normalFeatures = normal.map(email => tf.transform(email.split(" ")))

    val positiveExamples = spamFeatures.map(feature => LabeledPoint(1, feature))
    val negativeExamples = normalFeatures.map(feature => LabeledPoint(0, feature))
    val trainingData = positiveExamples.union(negativeExamples)
    trainingData.cache()

    // SGD 运行逻辑回归
    val model = new LogisticRegressionWithSGD().run(trainingData)

    val posTest = tf.transform("O M G GET cheap stuff by sending money to ...".split(" "))
    val negTest = tf.transform("Hi Dad, I started studying Spark the other ...".split(" "))

    println("Prediction for positive test example: " + model.predict(posTest))
    println("Prediction for negative test example: " + model.predict(negTest))
  }
}
