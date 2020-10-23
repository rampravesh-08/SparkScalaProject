package com.spark.scala.sqlex;
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StringType


object SparkDataFramePractice {
  def main(args: Array[String]): Unit={
    
    val spark= SparkSession.builder().config("spark.master","local").appName("DF Practice").getOrCreate();
    
    val columns = Seq("firstName","lastName","company","address","city","state","post","phone1","phone2","email","web")
    val schema = StructType(columns.map(col => StructField(col,StringType)))
    
    val df = spark.read.option("header",true).csv("C:\\RamPravesh\\BigData\\Dataset\\csv\\ca-500.csv");
    df.printSchema()
    
    val grpByrdd = df.groupBy("city").count
    grpByrdd.printSchema()
    grpByrdd.foreach(grp => {
      println(grp.getString(0) + "\t " + grp.getLong(1) )
    }
    )
   
    df.select("first_name", "email").show
    
    df.groupBy("company_name").count().sort().show(5)
    
    df.filter(df("first_name").like("a%")).show()
    df.filter(f => f.getString(4)=="Aurora").show()

    
  }
}