package com.spark.scala.sqlex

import org.apache.spark.sql.SparkSession

class SalesDataAnaylysisHive {
  def main(args: Array[String]): Unit = {
    
    val spark = SparkSession.builder
      .appName("my-app")
      .master("local[*]")
      .enableHiveSupport()
      .getOrCreate()
      
    val dataDF = spark.read.parquet("emp.parquet")
    dataDF.printSchema()
      
  }
}