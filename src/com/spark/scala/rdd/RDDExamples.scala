package com.spark.scala.rdd

import org.apache.spark.sql.SparkSession
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions


object RDDExamples {
  def main(args: Array[String]): Unit={
    
    val spark = SparkSession.builder().config("spark.master","local").appName("RDD Example").getOrCreate();
    
    val rdd = spark.sparkContext.textFile("C:\\RamPravesh\\BigData\\Dataset\\textpad2.txt");
    
    /*
    rdd.foreach(rec =>{
      println(rec)
    }
    */
    
    val rddM = rdd.flatMap(l=> (l.split(" "))).map(w=>(w,1))
    val red = rddM.reduceByKey((a,b) => a+b)
    red.foreach(println)
    
    val grpBy = rddM.groupByKey().map(l => (l._1+l._2))
    
    
    val reduc = rddM.values.reduce((a,b)=>a+b)
    println(reduc)
    
    val ramCnt = rdd.filter(l => l.contains("ram")).count()
    println(ramCnt)
    
    val x= spark.sparkContext.parallelize(Array(1,2,3), 2)
    val y= spark.sparkContext.parallelize(Array(3,4), 1)
    val z= x.union(y)
    z.foreach(println)
    
    z.collect().foreach(println)
    
  }
}