package com.spark.online.prob

import scala.io.Source
import scala.io.Codec
import java.nio.charset.CodingErrorAction

import org.apache.spark.sql.SparkSession
import org.apache.spark.rdd.RDD

object MostPopularMovei {
  
  def find_movie_name(m_id :String, movies:org.apache.spark.broadcast.Broadcast[Map[String, String]]):(String) = {
   val r = movies.value.filter{case (id,name) => id.equals(m_id) }
   r.get(m_id).get
  }
  
  def main(args: Array[String]): Unit={
    val spark = SparkSession.builder().appName("Movie Rating").config("spark.master","local").getOrCreate();
    
    
    // To handle : MalformedInputException thrown when an input byte sequence is not legal for given charset, or an input character sequence is not a legal sixteen-bit Unicode sequence.
    
    implicit val codec = Codec("UTF-8")
    codec.onMalformedInput(CodingErrorAction.REPLACE)
    codec.onUnmappableCharacter(CodingErrorAction.REPLACE)

    val m_name = Source.fromFile("C:\\RamPravesh\\BigData\\Dataset\\ml-100k\\u.item")
    val mapped_name = m_name.getLines().map(f => f.split('|')).map(r => (r(0),r(1))).toMap
    val movie_shared_val = spark.sparkContext.broadcast(mapped_name)
    
    
    val m_rdd = spark.sparkContext.textFile("C:\\RamPravesh\\BigData\\Dataset\\ml-100k\\u.data")
   
    val m_map_rdd1 = m_rdd.map(f => f.split("\t")).filter(f => f.length>3)
    val m_map_rdd = m_map_rdd1.map(rec => (rec(1),1))
    val m_red_rdd = m_map_rdd.reduceByKey( (a,b) => a+b )
    val swaped_rdd = m_red_rdd.map(f => (f._2,f._1)).sortByKey(false).map(f => (find_movie_name(f._2,movie_shared_val), f._1))
    
    swaped_rdd.foreach(println)
    
  }
}