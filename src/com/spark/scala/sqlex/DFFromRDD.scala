package com.spark.scala.sqlex

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.Row
import org.apache.spark.sql.functions.when
import org.apache.spark.sql.functions.sum

object DFFromRDD {
  def main(args: Array[String]): Unit={
    val spark = SparkSession.builder()
    .appName("RDD To DF")
    .master("local")
    .config("spark.sql.warehouse.dir", "hivewarehosedbloc")
    .enableHiveSupport()
    .getOrCreate();
    
    val columns = Seq("lang","user_count")
    val data = Seq(("Java", "20000"), ("Python", "100000"), ("Scala", "3000"))
    
    val rdd = spark.sparkContext.parallelize(data)
    
    import spark.implicits._
    val df = rdd.toDF(columns:_*)
    df.printSchema()
    df.explain()
    
    val schema = StructType(
    Array(
        StructField("lang",StringType,true),
        StructField("user_cnt",StringType, true)
      )    
    )
    val rowRDD = rdd.map(l => Row(l._1,l._2))
    val df2 = spark.createDataFrame(rowRDD, schema)
    df2.show()
    
    val columns_many = Seq("firstName","lastName","company","address","city","state","post","phone1","phone2","email","web")
    val dynamicSchema = StructType(columns_many.map(c=>StructField(c,StringType))) 
    
    
    val csvDF = spark.read
    .option("header", "true")
    .option("inferSchema", "true")
    .option("nullValue", "NA")
    .option("timestampFormat", "yyyy-MM-dd'T'HH:mm:ss")
    .option("mode", "failfast")
    .option("path", "C:\\RamPravesh\\BigData\\Dataset\\csv\\ca-500.csv")
    .csv()
    
    csvDF.show(5)
    
    val txtDF = spark.read.text("C:\\RamPravesh\\BigData\\Dataset\\csv\\ca-500.csv")
    val jsonDF = spark.read.json("C:\\RamPravesh\\BigData\\Dataset\\csv\\ca-500.csv")
    
    val df3 = csvDF.select($"postal", $"city")
    val df4 = df3.select($"postal", 
        (when($"city" === "Windsor", 1).otherwise(0)).alias("Windsor_Y"),
        (when($"city" === "Alcida", 1).otherwise(0)).alias("Alcida_Y")
    )
    
    val gr = df4.groupBy($"postal")
    val tt = gr.agg(sum($"Windsor_Y"), sum($"Alcida_Y"))
    tt.filter( $"sum(Windsor_Y)" >0 or $"sum(Alcida_Y)">0 ).show()
    // Jar: spark-xml_2.11
    /*
    val xmlDF = spark.read
    .format("com.databricks.spark.xml")
    .option("rowTag","Person")
    .xml("src/main/resources/persons.xml")
    */
    
   //val hiveDF = spark.sql("select * from emp")
    
    /*
    val mySql_df = spark.read
    .format("jdbc")
    .option("url", "jdbc:mysql://localhost:port/db")
    .option("driver","com.mysql.jdbc.Driver")
    .option("dbtable", "emp")
    .option("user","kram")
    .option("password", "password")
    .load()
    
    */
    
    
    
    
  }
}