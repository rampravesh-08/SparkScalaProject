package com.spark.scala.rdd

import org.apache.spark.sql.SparkSession
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions


object MostFamousSuperHero {
  def main(args: Array[String]): Unit = {
     val spark = SparkSession.builder().appName("DF Test").config("spark.master","local").getOrCreate();
    val rdd_hero_id = spark.sparkContext.textFile("C:\\RamPravesh\\BigData\\Dataset\\Marvel_Graph")
    
    val famous_super_hero = rdd_hero_id.map(line => line.split(" ") )
               .map(w => (w(0), w.length))
               .reduceByKey(_+_)
               .sortBy(_._2, false)
               .take(1)
    
    famous_super_hero.foreach(println)
  }
}