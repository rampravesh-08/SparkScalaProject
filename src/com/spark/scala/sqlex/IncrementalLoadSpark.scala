package com.spark.scala.sqlex

import org.apache.spark.sql.functions._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.sql.types.IntegerType

object IncrementalLoadSpark {
  def main(args: Array[String]): Unit = {
    
     val spark = SparkSession.builder
      .appName("my-app")
      .master("local[*]")
      .enableHiveSupport()
      .getOrCreate()
      
     val schema = StructType(
     List(
           StructField("id", IntegerType, nullable=true),
           StructField("name", StringType, nullable=true),
           StructField("salary", DoubleType, nullable=true),
           StructField("age", IntegerType, nullable=true),
           StructField("address", StringType, nullable=true),
           StructField("day", IntegerType, nullable=true)
         ) 
     )
      
     /*Incremental load in hive via HQL - Start*/
     val df = spark.read
     .schema(schema)
     .option("inferSchema", "true")
     .option("delimiter",",")
     .csv("day*.txt")
     
     df.createOrReplaceTempView("emp")
     spark.sql("select * from emp").show()
     //spark.sql("select id, max(day) as max_day from (select * from emp) d2 group by id").show()
     
     spark.sql("create temporary view inc_load_view as select e1.* from emp e1 join (select e2.id, max(e2.day) as day from emp e2 group by e2.id) s on s.id = e1.id and e1.day = s.day")
     
     spark.sql("select * from inc_load_view").sort(col("id").asc).show()
     /*Incremental load in hive via HQL- End*/
     
    
     /*Incremental load in hive via Spak SQL- Start*/
     val df_grouped = df.groupBy("id").agg(max("day").alias("day"))    
     df
     .join(df_grouped, (df("id") === df_grouped("id") &&  df("day") === df_grouped("day")), "leftsemi")
     .sort(df("id"))
     .show() // save into Hive table or HDFS or S3
      /*Incremental load in hive via Spak SQL- End*/
     
     
    /*Incremental load in hive via Spak SQL- Start*/
    val df1 = spark.read
     .schema(schema)
     .option("inferSchema", "true")
     .option("delimiter",",")
     .csv("day1.txt")
     
    val df2 = spark.read
     .schema(schema)
     .option("inferSchema", "true")
     .option("delimiter",",")
     .csv("day2.txt")
     
    val df12 = df1.union(df2).distinct()
    df12.show()
    val res  = df12.take(10)
    res.foreach(r =>{
      println(r(0),r.length)
    })
    

     
     Thread.sleep(100000)
     
      
  }
}