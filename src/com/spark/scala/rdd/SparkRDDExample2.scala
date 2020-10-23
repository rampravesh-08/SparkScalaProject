package com.spark.scala.rdd

import org.apache.spark.sql.SparkSession


object SparkRDDExample2 {
  def main(args: Array[String]): Unit={
    
    val spark = SparkSession.builder().config("spark.master","local").appName("RDD Example").getOrCreate();
    
    val ages= List(2, 52, 44,  23, 17,  14,  12, 82, 51, 64) 
    val rdd1 = spark.sparkContext.parallelize(ages)
    
    val grupedRdd = rdd1.groupBy(f =>{
      if(f >=18 && f <= 65) "Adult"
      else if (f < 18) "Minor"
      else "Old"
    })
    
    grupedRdd.foreach(println)
  }
}