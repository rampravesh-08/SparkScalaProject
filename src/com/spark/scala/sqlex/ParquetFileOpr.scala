package com.spark.scala.sqlex

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.avro._
import org.apache.avro.Schema
import java.io.File

object ParquetFileOpr {
  def main(args: Array[String]): Unit = {
    
    val spark = SparkSession.builder()
    .appName("RDD To DF")
    .master("local")
    .config("spark.sql.warehouse.dir", "hivewarehosedbloc")
    .enableHiveSupport()
    .getOrCreate();
     
    val data = Seq(("James ","","Smith","36636","M",3000),
                  ("Michael ","Rose","","40288","M",4000),
                  ("Robert ","","Williams","42114","M",4000),
                  ("Maria ","Anne","Jones","39192","F",4000),
                  ("Jen","Mary","Brown","","F",-1))
    
    val columns = Seq("firstname","middlename","lastname","dob","gender","salary")
    
    import spark.sqlContext.implicits._
    val df = data.toDF(columns:_*)
    
    df.printSchema()
  
    // Parquet ..............
    df.write
    .mode("overwrite")
    .partitionBy("gender")
    .parquet("emp.parquet")
    
    val df2 = spark.read
      .parquet("emp.parquet")
      .where("gender = 'F'")
    
    df2.show()
    
    val df3 = spark.read
      .parquet("emp.parquet")
      .where(col("gender") === "F")
      
    df3.show()
    
    // ORC
    df.write.mode("overwrite").partitionBy("gender").orc("emp.orc")
    spark.sql("create temporary table emp_orc using orc options (path \"emp.orc\")")
    spark.sql("select * from emp_orc").show()
    
    
    // Avro
    df.write
    .mode("overwrite")
    .partitionBy("gender")
    .format("avro")
    .option("path", "emp.avro")
    .save()
    
    val df_avro = spark.read
    .format("avro")
    .option("path", "emp.avro")
    .load()
    .where(col("salary") > 3000)
    
    df_avro.show()
    
    spark.sql("create temporary view emp_avro_v using avro options(path \"emp.avro/gender=M\")")
    spark.sql("select * from emp_avro_v").show()
    
    /* Uncomment when need to load the avro data via schema
    val schemaAvro = new Schema.Parser().parse(new File("src/main/resources/person.avsc"))
    val df_via_avro_schema = spark.read
              .format("avro")
              .option("avroSchema", schemaAvro.toString)
              .load("person.avro")
              * 
              */
              
    
    
  }
}