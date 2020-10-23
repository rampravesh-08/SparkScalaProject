package com.spark.scala.sqlex

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object SqlAggExample {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
    .appName("SQL Agg Practice")
    .master("local")
    .getOrCreate()
    
    val csvDF = spark.read
    .option("header", "true")
    .option("inferSchema", "true")
    .option("nullValue", "NA")
    .option("timestampFormat", "yyyy-MM-dd'T'HH:mm:ss")
    .option("mode", "failfast")
    .option("path", "C:\\RamPravesh\\BigData\\Dataset\\csv\\ca-500.csv")
    .csv()
    
    csvDF.show(5)
    
    val df_grp = csvDF.groupBy("city")
    val df_city_cnt = df_grp.agg(count("city"))
    
    
    import spark.implicits._
    val conDF = csvDF.where($"first_name".startsWith("F"))
    .groupBy("city").agg(count("city").as("c_cnt")).where($"c_cnt" > 1)
    conDF.show()
    
    val df = Seq(
  ("bar", 2L),
  ("bar", 2L),
  ("foo", 1L),
  ("foo", 2L)
).toDF("word", "num")


df
  .cube($"word", $"num")
  .count()
  .sort(asc("word"), asc("num"))
  .show()
  
    
    
    
  }
}