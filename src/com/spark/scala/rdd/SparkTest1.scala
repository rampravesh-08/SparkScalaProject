package com.spark.scala.rdd

import org.apache.spark.sql.SparkSession
import org.apache.spark.rdd.RDD.rddToOrderedRDDFunctions
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions

case class Employee(firstName: String, lastName: String, company: String, address: String, city: String, state: String, post: String, phone1: String, phone2: String, email: String, web: String)

object SparkTest1 {
  def main(args: Array[String]): Unit={
    
    val spark = SparkSession.builder().appName("Test Job").config("spark.master", "local").getOrCreate();
    
    val rdd = spark.sparkContext.textFile("C:\\RamPravesh\\BigData\\Dataset\\csv\\au-500.csv", 2);
    val rddWIthColmnsHeader = rdd.map{
      line => 
        val col = line.split(",")
        Employee(col(0),col(1),col(2),col(3),col(4),col(5),col(6),col(7),col(8),col(9),col(10))
    }
    
    import spark.implicits._
    val empDF = rddWIthColmnsHeader.toDF();
   // empDF.show();
    
    val df = spark.read.csv("C:\\RamPravesh\\BigData\\Dataset\\csv\\au-500.csv");
    //df.show();
    
    val rddWords = rdd.flatMap(line => line.split(","))
    val wordMap = rdd.map(w => (w,1))
    //val filterMap = wordMap.filter(w=> w._1.startsWith("R"))
    val reduceMap = wordMap.reduceByKey(_+_)
    //reduceMap.foreach(println)   
    val sapKeyVal = reduceMap.map(a => (a._2,a._1))
    val soryByKey = sapKeyVal.sortByKey()
    
    val rddrr = spark.sparkContext.parallelize(List(1,2,3,4,5,6))
    val reduceT = rddrr.reduce(_+_);
    
    soryByKey.foreach(println)
    
    
    
  }
}