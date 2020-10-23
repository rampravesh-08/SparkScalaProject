package com.spark.scala.sqlex

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.SaveMode

object CombineTwoDataFrame {
  
  def main(args: Array[String]): Unit ={
    val spark = SparkSession.builder
      .appName("my-app")
      .master("local[*]")
      .enableHiveSupport()
      .getOrCreate()

      //Reading csv file.
      val fsprdDF = spark.read.option("header","true").option("inferschema", "true")
      .csv("src/main/resources/Periods-2015-_2022.csv")
      
      //Create database1 and table1
      spark.sql("create database if not exists database1")
      fsprdDF.write.mode(SaveMode.Overwrite).saveAsTable("database1.table1")
      
      //Create database2 and table2
      spark.sql("create database if not exists database2")
      fsprdDF.write.mode(SaveMode.Overwrite).saveAsTable("database2.table2")
      
      //UNION Operation
      val unionTblist = List("database1.table1", "database2.table2")
      var trgTable = spark.emptyDataFrame
      var dbTbllist = Array(Seq(""))
      
      /*
      for (tbl <- unionTblist) {
         val srctable = spark.table(tbl)
         dbTbllist = dbTbllist :+ srctable
      }
      if (dbTbllist.length > 1) {
         trgTable = dbTbllist.reduce(_ union _)
      } else {
         trgTable = dbTbllist(0)
      }
      //Create database3.table1 with union of database1.table1 and database.table2
      spark.sql("create database if not exists database3")
       trgTable
          .distinct
          .write
          .mode(SaveMode.Overwrite).saveAsTable("database3.table1")
*/
    
  }
}