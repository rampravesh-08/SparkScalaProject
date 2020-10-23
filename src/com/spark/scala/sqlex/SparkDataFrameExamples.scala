package com.spark.scala.sqlex;
import org.apache.spark.sql.SparkSession

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.functions.when
import org.apache.spark.sql.functions.to_timestamp
import org.apache.spark.sql.functions.unix_timestamp
import org.apache.spark.sql.functions.to_date
import org.apache.spark.sql.functions.year
import org.apache.spark.sql.functions.month
import org.apache.spark.sql.functions.dayofmonth
import org.apache.spark.sql.functions.dayofweek
import org.apache.spark.sql.functions.dayofyear
import org.apache.spark.sql.functions.days
import org.apache.spark.ui.SparkUI
import org.apache.spark.SparkContext

object SparkDataFrameExamples {
  def main(args: Array[String]): Unit={
    
    val spark = SparkSession.builder().config("spark.master","local").appName("DataFrame").getOrCreate();
    
    val df = spark.read.option("header", true).csv("C:\\RamPravesh\\BigData\\Dataset\\nyc_taxi_trip_duration.csv");
    val df_ven = spark.read.option("header", true).csv("C:\\RamPravesh\\BigData\\Dataset\\vendors.csv")

    df.printSchema();
    
    val distinct_df = df.select("vendor_id").distinct()
    //distinct_df.show()
    
    val dd=  df.join(df_ven, Seq("vendor_id"), "inner") // left, right, fullouter, 
    dd.show()
    
    val sub_df = df.select("id", "pickup_datetime", "dropoff_datetime", "passenger_count", "trip_duration","start_datetime","end_datetime")
    
    val sub_df_1 = sub_df.withColumn("actual_trip_dur", 
         when(col("start_datetime").isNotNull && col("end_datetime").isNotNull, (unix_timestamp(col("end_datetime"))-unix_timestamp(col("start_datetime"))))
        .when(col("start_datetime").isNotNull && col("end_datetime").isNull , (unix_timestamp(col("dropoff_datetime"))-unix_timestamp(col("start_datetime"))))
        .otherwise(unix_timestamp(col("dropoff_datetime"))-unix_timestamp(col("pickup_datetime"))
    ))
    sub_df_1.show()
    
    //sub_df.withColumn("trip_dur_sec", unix_timestamp(col("dropoff_datetime"))-unix_timestamp(col("pickup_datetime")/60)).show()
        
     /*
     sub_df.withColumn("pickup_year", year(col("pickup_datetime")))
     .withColumn("pickup_month", month(col("pickup_datetime")))
     .withColumn("pickup_days", days(col("pickup_datetime")))
     .withColumn("pickup_dayofmonth", dayofmonth(col("pickup_datetime")))
     .withColumn("pickup_dayofweek", dayofweek(col("pickup_datetime")))
     .withColumn("pickup_dayofyear", dayofyear(col("pickup_datetime")))
     .show()   
     */
     
     val df_dmy = sub_df.withColumn("pickup_year", year(col("pickup_datetime")))
     .withColumn("pickup_dayofweek", dayofweek(col("pickup_datetime")))
     .withColumn("pickup_dayofmonth", dayofmonth(col("pickup_datetime")))
     .withColumn("pickup_dayofyear", dayofyear(col("pickup_datetime")))
    
     df_dmy.groupBy("pickup_dayofweek").count().show
     
     df_dmy.filter(col("trip_duration") > 30*60 && col("passenger_count") <= 2).groupBy("pickup_dayofweek","pickup_dayofmonth","pickup_year").count().show
     
     df_dmy.createTempView("pickup")
     spark.sql("select * from pickup").show
     

   
    
  }
}