package com.spark.scala.sqlex

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object MinMaxDF {
  def main(args: Array[String]): Unit = {
    
    val spark = SparkSession.builder()
    .appName("Min Max Test")
    .master("local")
    //.config("spark.sql.warehouse.dir", "hivewarehosedbloc")
    //.enableHiveSupport()
    .getOrCreate();
    
    val df = spark.read
      .option("header", "true")
      .option("delimiter", ";")
      .option("inferSchema", "true")
      .option("path", "C:\\RamPravesh\\BigData\\Dataset\\bank.csv")
      .csv()
      
     df.printSchema()
     df.show()
     
     val df2 = df.groupBy("month")
             .agg(sum("duration").as("tot_dur"))
             .sort(desc("tot_dur"))
             
     val top_10 = df2.head(10)
     top_10.foreach(r => {
       println(r)
     })
    
  }
}