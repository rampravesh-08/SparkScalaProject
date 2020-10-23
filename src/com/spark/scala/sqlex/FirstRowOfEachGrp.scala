package com.spark.scala.sqlex

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.row_number
import org.apache.spark.sql.functions.avg
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.functions._

object FirstRowOfEachGrp {
  def main(args: Array[String]): Unit={
    
   val spark = SparkSession.builder()
    .appName("Day to day opeatoins")
    .master("local")
    .getOrCreate()
    
 val simpleData = Seq(("James","Sales","NY",90000,34,10000),
    ("Michael","Sales","NY",86000,56,20000),
    ("Robert","Sales","CA",81000,30,23000),
    ("Maria","Finance","CA",90000,24,23000),
    ("Raman","Finance","CA",99000,40,24000),
    ("Scott","Finance","NY",83000,36,19000),
    ("Jen","Finance","NY",79000,53,15000),
    ("Jeff","Marketing","CA",80000,25,18000),
    ("Kumar","Marketing","NY",91000,50,21000)
  )
  import spark.implicits._
    val df = simpleData.toDF("employee_name","Department","state","Salary","age","bonus")
    df.show()
    
    val wind1 = Window.partitionBy("Department").orderBy("Salary")
    val df2 = df.withColumn("row", row_number.over(wind1))
    df2.filter("row = '1'").show()
    
    val wind2 = Window.partitionBy("Department").orderBy("Salary")
    val df3 = df.withColumn("row", row_number.over(wind2))
    .withColumn("avg", avg(col("Salary")).over(wind2))
    .where(col("row") === 1)
    df3.show()
    
    df.groupBy("Department").agg(avg(col("Salary"))).show()
    
    df.sort("Department","state").show()
    
    df.sort(col("Department"),col("state"), col("Salary").desc).show()
    
    //df.select($"Department", desc("Salary")).show()
    
    df.orderBy(col("Department"),col("state"), col("Salary").desc).show();
     
     
    
  }
}