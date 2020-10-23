package com.spark.scala.rdd

import org.apache.spark.sql.SparkSession
import scala.collection.Seq


object SparkJoinExamples {
  def main(args: Array[String]): Unit={
    
        val emp = Seq((1,"Smith",-1,"2018","10","M",3000),
        (2,"Rose",1,"2010","20","M",4000),
        (3,"Williams",1,"2010","10","M",1000),
        (4,"Jones",2,"2005","10","F",2000),
        (5,"Brown",2,"2010","40","",-1),
          (6,"Brown",2,"2010","50","",-1)
      )
      val empColumns = Seq("emp_id","name","superior_emp_id","year_joined",
           "emp_dept_id","gender","salary")
           
      val spark = SparkSession.builder().config("spark.master","local").appName("Join Examples").getOrCreate();       
           
      import spark.implicits._
      val empDF = emp.toDF(empColumns:_*)
      empDF.show(false)
    
      val dept = Seq(("Finance",10),
        ("Marketing",20),
        ("Sales",30),
        ("IT",40)
      )
    
      val deptColumns = Seq("dept_name","dept_id")
      val deptDF = dept.toDF(deptColumns:_*)
      deptDF.show(false)
      
      val fullJoin_df = empDF.join(deptDF, empDF("emp_dept_id") === deptDF("dept_id"), "full")
      fullJoin_df.show()
      

  }
}