
package com.spark.scala.rdd

import org.apache.spark.sql.SparkSession
import scala.collection.Seq

object SparkTestObject {
  
  def mapper_fun(rec: Seq[Any]): (Any,Seq[Any]) = {
    return (rec(5),rec)
  }
 
  
  def main(args: Array[String]): Unit={
    
    val spark = SparkSession
    .builder()
    .config("spark.master","local")
    .appName("Test")
    .getOrCreate();
    
    val rdd = spark.sparkContext.parallelize(
    Seq(Seq(1, "abc1", 10000, 10002, 8, "driver"),
    Seq(2, "abc2", 10000, 10002, 8, "passenger"),
    Seq(3, "abc3", 10003, 10002, 8, "passenger"),
    Seq(4, "abc4", 10000, 10002, 9, "driver"),
    Seq(5, "abc5", 10000, 10002, 8, "passenger"),
    Seq(6, "abc6", 10000, 10002, 8, "passenger"),
    Seq(7, "abc7", 10000, 10002, 8, "driver")))   
    
    
    val rdd2 = rdd.map(mapper_fun)
    rdd2.foreach(println)
    
    /*
    import spark.implicits._
    val df = rdd.toDF()
    df.show()
    * 
    */
    
  }
}