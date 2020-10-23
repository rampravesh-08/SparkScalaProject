
package com.spark.scala.rdd

import org.apache.spark.sql.SparkSession
import scala.collection.mutable.ListBuffer
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions
import scala.collection.Seq

object SparkPayPalInterviewQues1 {
  def main(args: Array[String]): Unit={
    
    val spark = SparkSession
    .builder()
    .config("spark.master","local")
    .appName("Test")
    .getOrCreate();
    
    val rdd = spark.sparkContext.parallelize(
    Seq(Seq(1, "abc1", 10000, 10002, 8, "driver"),
    Seq(2, "abc2", 10000, 10002, 8, "passenger"),
    Seq(3, "abc3", 10003, 10002, 8, "passenger"),
    Seq(4, "abc4", 10000, 10002, 9, "driver"),
    Seq(5, "abc5", 10000, 10002, 8, "passenger"),
    Seq(6, "abc6", 10000, 10002, 8, "passenger"),
    Seq(7, "abc7", 10000, 10002, 8, "driver")))   
    
    // Expecion (1, 2, 5)(3)(4)(6, 7)
    
    val mappedRdd = rdd.map(f => ("trnx", f))
    val groupedRdd = mappedRdd.groupByKey()
    val cabAllcationQueue = groupedRdd.map{case(k,v) =>{
      
    var buffer = new ListBuffer[Any]
    val driverStack = new ListBuffer[Any]
    val passengerStack = new ListBuffer[Any]
    
    var count = 0
    v.foreach(w => {        
      if(w(5).equals("driver")) driverStack.+=(w)
      else  passengerStack.+=(w)
      
    }) 
    
    val res = new ListBuffer[Any]
    driverStack.foreach(driver => {
      var trip = new ListBuffer[Any]
      val drv = driver.asInstanceOf[List[String]]
      trip.+=(drv)
      
      var index = 0
      var count = 0
      passengerStack.foreach(pasngr => {
        var psng = pasngr.asInstanceOf[List[String]]
        if(count < 2
            && psng(2) == drv(2)
            && psng(3) == drv(3)
            && psng(4) == drv(4)){
          trip.+=(psng)
          
          passengerStack.remove(index) // Pop-up the selected record in trip
          index -=1
          count +=1
        }
        index += 1
      })
      res.+=(trip)
    })
    
    passengerStack.foreach(pasngr => {
       var trip = new ListBuffer[Any]
       trip.+=(pasngr)
       res.+=(trip)
    })
    
    (res)
   }
  }
  cabAllcationQueue.foreach(println) 
  }
}