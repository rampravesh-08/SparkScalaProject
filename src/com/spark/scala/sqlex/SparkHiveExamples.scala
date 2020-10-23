package com.spark.scala.sqlex;
import org.apache.spark.sql.SparkSession



object SparkHiveExamples {
  def main(args: Array[String]): Unit={
    
    val spark = SparkSession.builder()
    .appName("Hive Example")
    .config("spark.sql.warehouse.dir", "hivewarehosedbloc")
    .enableHiveSupport()
    .master("local[*]")
    .getOrCreate()
    
    import spark.implicits._
    import spark.sql
    
    sql("create table if not exists table1(key INT, value STRING) using hive ")
    sql("load data local inpath 'kv1.txt' into table table1")
    
    sql("select * from table1").show()
   
  }
}