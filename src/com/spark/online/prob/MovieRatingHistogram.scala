package com.spark.online.prob

import org.apache.spark.sql.SparkSession
import scala.collection.immutable.ListMap


object MovieRatingHistogram {
  def main(args: Array[String]): Unit={
    val spark = SparkSession.builder().config("spark.master","local").appName("Histogram").getOrCreate();
    
    val movie_rdd = spark.sparkContext.textFile("C:\\RamPravesh\\BigData\\Dataset\\ml-100k\\u.data")
    val rating_rdd = movie_rdd.map(f => f(2))
    
    val rating_cnt = rating_rdd.countByValue()
    val sorted_rating_acending_cnt = ListMap(rating_cnt.toSeq.sortBy(_._2):_*)
    
    sorted_rating_acending_cnt.foreach(println)
    
    val sorted_rating_decending_cnt = ListMap(rating_cnt.toSeq.sortWith(_._2>_._2):_*)
    sorted_rating_decending_cnt.foreach(println)
    for( (k,value) <- sorted_rating_decending_cnt){
      println(k + "     " + value)
    }
    
    val rating_rddm = movie_rdd.map(f => (f(2), f))
    val rating_rddm1 = rating_rddm.mapValues(f => (f(2).toInt,1))
    val rating_redk = rating_rddm1.reduceByKey((x,y) => ((x._1+y._1), (x._2+y._2)))
    val rating_avg = rating_redk.mapValues(f => f._1/f._2 )
    
    val keyv = rating_avg.lookup('4')
    println("Avg for 4 ", keyv)
    
    val collect_avg = rating_avg.collect()
    println("Rating   Avg")
    for( rat_avg <- rating_avg){
      println(rat_avg._1 + "\t"+ rat_avg._2)
    }
   
    
    
  }
}