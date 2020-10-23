package com.spark.scala.sqlex

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.functions.when
import org.apache.spark.sql.functions.expr

object DFDayToDayOperation {
  def main(args: Array[String]): Unit ={
    val spark = SparkSession.builder()
    .appName("Day to day opeatoins")
    .master("local")
    .getOrCreate()
    
    val data = Seq(Row(Row("James","","Smith"),"36636","M",3000),
      Row(Row("Michael ","Rose",""),"40288","M",4000),
      Row(Row("Robert ","","Williams"),"42114","M",4000),
      Row(Row("Maria ","Anne","Jones"),"39192","F",4000),
      Row(Row("Jen","Mary","Brown"),"","F",-1)
    )
    
      val schema = new StructType()
        .add("name",new StructType()
        .add("firstname",StringType)
        .add("middlename",StringType)
        .add("lastname",StringType))
        .add("dob",StringType)
        .add("gender",StringType)
        .add("salary",IntegerType)
  
    val df = spark.createDataFrame(spark.sparkContext.parallelize(data),schema)
    
    df.printSchema()
    
    df.show()
    
    df.withColumnRenamed("salary", "Salary")
    df.drop("dob").show()
    
    df.filter(f => f.get(2)=="M").show()
    df.filter(f => f.get(0).toString().contains("James")).show()
    df.filter(df("name.firstname") === "James").show()
    
    df.filter("gender =='M'").show()
    df.where("gender =='F'").show()
    
    df.withColumn("Gender_Desc", 
     when(col("gender") === "F", "Female")
    .when(col("gender") === "M", "Male")
    .otherwise("UnKown")
    ).show()
    
    
    df.withColumn("Gender_Desc", 
    expr("case when gender = 'M' then 'Male' when gender 'F' then 'Female' else 'Unkown' ")    
    ).show()

  }
}