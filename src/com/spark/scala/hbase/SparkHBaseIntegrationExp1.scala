package com.spark.scala.hbase;
import org.apache.spark.sql.SparkSession
import org.apache.hadoop.hbase.spark.datasources.HBaseTableCatalog
import org.apache.hadoop.hbase.spark.datasources.HBaseTableCatalog

case class Emp(key: String, firstName: String, lastName: String, middleName: String, addressLine: String, city: String, state: String, zipCode: String)

object SparkHBaseIntegrationExp1 {
  
  def main(args: Array[String]): Unit={
    
  val defcatalog=
            s"""{
            |"table":{"namespace":"default","name":"employee"},
            |"rowkey":"key",
            |"columns":{
            |"key":{"cf":"rowkey","col":"key","type":"string"},
            |"fName":{"cf":"person","col":"firstName","type":"string"},
            |"lName":{"cf":"person","col":"lastName","type":"string"},
            |"mName":{"cf":"person","col":"middleName","type":"string"},
            |"addressLine":{"cf":"address","col":"addressLine","type":"string"},
            |"city":{"cf":"address","col":"city","type":"string"},
            |"state":{"cf":"address","col":"state","type":"string"},
            |"zipCode":{"cf":"address","col":"zipCode","type":"string"}
            |}
            |}""".stripMargin

            
            
      val data=Seq(Emp("1","Abby","Smith","K","3456main","Orlando","FL","45235"),
      Emp("2","Amaya","Williams","L","123Orange","Newark","NJ","27656"),
      Emp("3","Alchemy","Davis","P","Warners","Sanjose","CA","34789"))
      
      val spark=SparkSession.builder()
      .master("local[1]")
      .appName("SparkByExamples.com")
      .getOrCreate()
      
      import spark.implicits._
      val df=spark.sparkContext.parallelize(data).toDF

      
      df.write.options(
      Map(HBaseTableCatalog.tableCatalog->defcatalog,HBaseTableCatalog.newTable->"4"))
      .format("org.apache.spark.sql.execution.datasources.hbase")
      .save()
      
      
      val hbaseDF=spark.read
      .options(Map(HBaseTableCatalog.tableCatalog->defcatalog))
      .format("org.apache.spark.sql.execution.datasources.hbase")
      .load()
      
      hbaseDF.printSchema()
      
      hbaseDF.show(false)
      
      hbaseDF.filter($"key"==="1"&&$"state"==="FL")
      .select("key","fName","lName")
      .show()


      hbaseDF.createOrReplaceTempView("employeeTable")
      spark.sql("select*fromemployeeTablewherefName='Amaya'").show
      
      

  }
}