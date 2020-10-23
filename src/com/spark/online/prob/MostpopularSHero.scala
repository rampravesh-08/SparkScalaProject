package com.spark.online.prob

import scala.io.Source
import scala.io.Codec
import java.nio.charset.CodingErrorAction

import org.apache.spark.sql.SparkSession

object MostpopularSHero {
  
  def fetch_sh_name(sh_id: (Int,String), sh_lookup: Map[String, String] ):( Map[String, String]) ={
   sh_lookup.filter(p => (p._1.toInt == sh_id._1))
  }
  
  def main(args: Array[String]): Unit={
    val spark = SparkSession.builder().appName("Most Popular Super-hero").config("spark.master","local").getOrCreate()
    
    
    implicit val codec = Codec("UTF-8")
    codec.onMalformedInput(CodingErrorAction.REPLACE)
    codec.onUnmappableCharacter(CodingErrorAction.REPLACE)
    val sh_name = Source.fromFile("C:\\RamPravesh\\BigData\\Dataset\\Marvel_Names")
    val sh_name_map = sh_name.getLines().map(f => f.split(" ")).map(f => (f(0),f(1))).toMap
    
    val super_hero_lookup = spark.sparkContext.broadcast(sh_name_map)
    
    val rdd_hero_id = spark.sparkContext.textFile("C:\\RamPravesh\\BigData\\Dataset\\Marvel_Graph")
    val rdd_hero_id_m = rdd_hero_id.map(f => f.split(" ")).map(w => (w(0),w.length-1))
    val rdd_hero_cnt = rdd_hero_id_m.reduceByKey((a,b) => a+b)
    
    val flipped_cnt = rdd_hero_cnt.map(f => (f._2,f._1))
    
    val famous_sh = flipped_cnt.max()
    val famous_sh1 = fetch_sh_name(famous_sh,sh_name_map)
    famous_sh1.foreach(println)
    
    
    
  }
}