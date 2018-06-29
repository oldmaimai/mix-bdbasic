package com.huaan.bd.mllib

import org.apache.spark.mllib.classification.LogisticRegressionWithSGD
import org.apache.spark.mllib.feature.HashingTF
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.{SparkConf, SparkContext}

object Spam {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Spam example")
    //.setMaster("local[*]")
    val sc = new SparkContext(conf)
    val spam = sc.textFile("/home/mai/software/spark/spark-1.6.0-bin-hadoop2.6/testjars/spam.txt")
    val normal = sc.textFile("/home/mai/software/spark/spark-1.6.0-bin-hadoop2.6/testjars/ham.txt")

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
