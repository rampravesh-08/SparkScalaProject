package com.spark.scala.sqlex;
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StructField

import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.Row


object SparkDFTest1 {
  def main(args: Array[String]): Unit={
    val spark = SparkSession.builder().appName("DF Test").config("spark.master","local").getOrCreate();
    import spark.implicits._
    
    val columns = Seq("language","usercnt");
    val data = Seq(("java",2000),("Python",3000),("Scala",5000))
    val data1 = spark.sparkContext.parallelize(data)
    
    val dataCol1 = data1.toDF("language","usercnt")
    dataCol1.printSchema()
    dataCol1.show()
    
    val dataDF2 = spark.createDataFrame(data1).toDF(columns: _*)
    dataDF2.show()
    
   
    val schema = StructType(columns.map( col=> StructField(col, StringType, nullable=true )))
    val rowRDD = data1.map(line => Row(line._1,line._2))
    val dataDF3 = spark.createDataFrame(rowRDD, schema)
   // dataDF3.show()
    
    
    val dataDF4 = data.toDF();
    println("DF4")
    dataDF4.show()
    
    val dataDF5 = spark.createDataFrame(data).toDF(columns: _*);
    dataDF5.show()
    
    val df6 = spark.read.csv("C:\\RamPravesh\\BigData\\Dataset\\csv\\au-500.csv");
    df6.show()
    
    val df7 = spark.read.text("C:\\RamPravesh\\BigData\\Dataset\\text.txt")
    df7.show()
    
    
    // XML com.databricks.spark.xml
    //val dfXML = spark.read.format("com.databricks.spark.xml").option("rowTag", "Person").xml("emp.xml");
    
    //val hiveContext = new org.apache.spark.sql.hive.HiveContext()
    //val hiveDF = hiveContext.sql("select * from emp")
    
    // DF via JDBC
    val my_sql = spark.read.format("jdbc")
    .option("url","jdbc:mysql://localhost:port/db")
    .option("driver", "com.mysql.jdbc.Driver")
    .option("dbtable","emp")
    .option("username", "dbuser")
    .option("password", "password")
    .load()
    
  }
}