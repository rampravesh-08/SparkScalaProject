package com.spark.scala.sqlex;
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

case class DSEmployee(id: String, vendor_id: String, pickup_datetime: String, dropoff_datetime: String, passenger_count: String, pickup_longitude: String, pickup_latitude: String, dropoff_longitude: String, dropoff_latitude: String, store_and_fwd_flag: String, trip_duration: String)

object SparkDatasetExamples {
  
  def main(args: Array[String]): Unit={
  
  val spark = SparkSession.builder().config("spark.master","local").appName("Dataset Example").getOrCreate();
  
  import spark.implicits._
  val ds1 = spark.read.option("header", true).csv("C:\\RamPravesh\\BigData\\Dataset\\nyc_taxi_trip_duration.csv").as[DSEmployee]
  
  ds1.printSchema()
  
  val ds2 = ds1.select("id", "pickup_datetime", "dropoff_datetime", "passenger_count", "trip_duration")
  
  ds2.withColumn("trip_dur", unix_timestamp(col("dropoff_datetime"))-unix_timestamp(col("pickup_datetime")) ).show()
  
  val ds3 = ds2.withColumn("pickup_year", year(col("pickup_datetime")))
  .withColumn("pickup_month_day", dayofmonth(col("pickup_datetime")))
  .withColumn("pickup_week_day", dayofweek(col("pickup_datetime")))
  .withColumn("pickup_month", month(col("pickup_datetime")))
  
  println(ds3.count())
  
  ds3.persist()
  
  //ds3.groupBy("pickup_month").count().show()
  
  //ds3.groupBy("pickup_week_day", "pickup_month","pickup_year").count().show()
  
  //ds3.filter(col("passenger_count") <=2 && col("trip_duration") > 30*60).groupBy("pickup_week_day", "pickup_month","pickup_year").count().show
  
  /*
  ds3.collect().foreach(rec => {
  	println(rec)  
  })
  */
  
  
  
  }
}
