package com.xad.spark.core

import org.apache.spark.{SparkConf, SparkContext}

/**
 *
 * @version 1.0
 * @author xad
 * @date 2021/3/12
 */
object Spark_WorldCount {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local").setAppName("WorldCount")
    val sc = new SparkContext(sparkConf)

    val lines = sc.textFile("./spark/datas")
    val words = lines.flatMap(_.split(" "))
    val group = words.groupBy(word => word)

    val count = group.map{
      case ( word, list) => {
        (word, list.size)
      }
    }

    val array = count.collect()
    array.foreach(println)
    sc.stop()
  }
}
