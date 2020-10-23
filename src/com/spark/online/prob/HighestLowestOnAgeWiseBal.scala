package com.spark.online.prob

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.min

object HighestLowestOnAgeWiseBal {
  def main(args: Array[String]): Unit={
    
    val spark = SparkSession.builder().config("spark.master","local").appName("Min-max Test").getOrCreate();
    
    val bank_rdd = spark.sparkContext.textFile("C:\\RamPravesh\\BigData\\Dataset\\bank.csv", 3)
    
    val bank_srdd = bank_rdd.map(rec => { rec.split(";")} ).filter(f=> (f(0)!="\"age\""))
    
    //println(bank_srdd.first())
    
   
    val bank_srdd1 = bank_srdd.map(l => (l(10),  l(11).toInt ))
    
    
    val min_month_wise = bank_srdd1.reduceByKey((a,b) => if(a<b) a else b ).map(r => (r._2,r._1)).sortByKey()
    val max_month_wise = bank_srdd1.reduceByKey((a,b) => if(a>b) a else b).map(r => (r._2,r._1)).sortByKey(false)
     
    min_month_wise.collect().foreach(println)
    max_month_wise.collect().foreach(println)
    
  }
}