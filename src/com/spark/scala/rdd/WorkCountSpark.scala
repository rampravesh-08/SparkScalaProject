package com.spark.scala.rdd

import org.apache.spark.sql.SparkSession
import org.apache.spark.rdd.RDD.numericRDDToDoubleRDDFunctions
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions


object WorkCountSpark {
  def main(args: Array[String]): Unit={
    val spark = SparkSession.builder().appName("Word Count").config("spark.master","local").getOrCreate();
    
    val rdd = spark.sparkContext.textFile("C:\\RamPravesh\\BigData\\Dataset\\text.txt", 2);
    val rddMap = rdd.flatMap(l=> l.split(" ")).map(w=> (w,1))
    val wordCnt = rddMap.reduceByKey((a,b) =>(a+b))
    wordCnt.foreach(f=>{
      
      println(f._1 + " = "+ f._2)
      
    }
    )
    
    val distictWordCnt = rddMap.distinct()
    val wordDistinctCnt = distictWordCnt.values.sum()
    println("Total Number of Distict Words : "+ wordDistinctCnt + " " + distictWordCnt.count() + " "+wordCnt.count())
    
  }
}